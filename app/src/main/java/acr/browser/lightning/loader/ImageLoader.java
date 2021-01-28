package acr.browser.lightning.loader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import acr.browser.lightning.R;


@SuppressLint("NewApi")
public class ImageLoader {

	MemoryCache memoryCache = new MemoryCache();
	FileCache fileCache;

	private Map<ImageView, String> imageViews = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());
	ThreadPoolExecutor executorService;
	Handler handler = new Handler();// handler to display images in UI thread
	final int noImageResId, notAvailableImageResId;
	final int REQUIRED_SIZE = 256;

	@SuppressLint("NewApi")
	public ImageLoader(Context context, boolean sdcard, String folderName,
                       int noImageResId, int notAvailableImageResId) {
		fileCache = new FileCache(context, sdcard, folderName);
		initExecutor();
		this.noImageResId = noImageResId;
		this.notAvailableImageResId = notAvailableImageResId;
	}

	private void initExecutor() {
		// executorService = Executors.newFixedThreadPool(5);
		executorService = new ThreadPoolExecutor(4, 16, 30, TimeUnit.SECONDS,
				new LifoBlockingDeque<Runnable>());
		executorService.allowCoreThreadTimeOut(true);
	}

	public ImageLoader(Context context, boolean sdcard, String folderName) {
		this(context, sdcard, folderName, R.drawable.ic_webpage,
				R.drawable.ic_webpage);
	}

	public void displayImage(String url, ImageView imageView, boolean bScale) {
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null) {
			if (imageView == null)
				return;
			imageViews.put(imageView, url);
			imageView.setImageBitmap(bitmap);
			imageView.setTag(bitmap);
		} else {
			if (imageView != null) {
				imageView.clearAnimation();
				imageViews.put(imageView, url);
				imageView.setImageResource(noImageResId);

			}
			queuePhoto(url, imageView, bScale);
		}
	}

	public void removeImCache(String url) {
		Bitmap b = memoryCache.remove(url);
		Log.e("bitmap", "b=" + b);
		File f = fileCache.getFile(url);
		if (f != null)
			f.delete();
	}

	/** if imageView never used after that */
	public void displayImage1time(String url, ImageView imageView,
                                  boolean bScale) {
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
			imageView.setVisibility(View.VISIBLE);
		} else {
			imageView.clearAnimation();
			imageView.setImageResource(noImageResId);
			queuePhoto(url, imageView, bScale);
		}
	}

	public void removeImageView(ImageView im) {
		imageViews.remove(im);
	}

	public void resetListView() {
		imageViews.clear();
	}

	public void reset() {
		imageViews.clear();
		executorService.shutdown();
		initExecutor();
	}

	private void queuePhoto(String url, ImageView imageView, boolean bScale) {
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		executorService.execute(new PhotosLoader(p, bScale));
	}

	public Bitmap getBitmapInCache(String url, boolean bScale) {
		File f = fileCache.getFile(url);

		// from SD cache
		Bitmap b = decodeFile(f, bScale);
		if (b != null) {
			return b;
		} else {
			return null;
		}
	}

	public Bitmap getBitmap(String url, boolean bScale) {
		File f = fileCache.getFile(url);

		// from SD cache
		Bitmap b = decodeFile(f, bScale);
		if (b != null)
			return b;

		// from web
		try {
			Util.download2file(url, f);
			return decodeFile(f, bScale);
		} catch (Throwable ex) {
			ex.printStackTrace();
			if (ex instanceof OutOfMemoryError)
				memoryCache.clear();
			return null;
		}
	}

	// decodes image and scales it to reduce memory consumption
	private Bitmap decodeFile(File f, boolean bScale) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			FileInputStream stream1 = new FileInputStream(f);
			BitmapFactory.decodeStream(stream1, null, o);
			stream1.close();
			int scale = 1;
			if (bScale) {
				// Find the correct scale value. It should be the power of 2.
				int width_tmp = o.outWidth, height_tmp = o.outHeight;
				while (true) {
					if ((width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE))
						break;
					width_tmp /= 2;
					height_tmp /= 2;
					scale *= 2;
				}
			}
			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			FileInputStream stream2 = new FileInputStream(f);
			Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
			stream2.close();
			return bitmap;
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;// */
	}

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;

		public PhotoToLoad(String u, ImageView i) {
			url = u;
			imageView = i;
		}
	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;
		private boolean mBScale = true;

		PhotosLoader(PhotoToLoad photoToLoad, boolean bScale) {
			this.photoToLoad = photoToLoad;
			mBScale = bScale;
		}

		@Override
		public void run() {
			try {
				if (imageViewReused(photoToLoad))
					return;
				if (photoToLoad.imageView == null) {
					File f = fileCache.getFile(photoToLoad.url);
					if (f.exists())
						return;
					Util.download2file(photoToLoad.url, f);
				} else {
					Bitmap bmp = getBitmap(photoToLoad.url, mBScale);
					memoryCache.put(photoToLoad.url, bmp);
					if (imageViewReused(photoToLoad))
						return;
					BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
					handler.post(bd);
				}
			} catch (Throwable th) {
				th.printStackTrace();
			}
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof PhotosLoader) {
				return photoToLoad.url
						.equals(((PhotosLoader) o).photoToLoad.url);
			}
			return super.equals(o);
		}
	}

	boolean imageViewReused(PhotoToLoad photoToLoad) {
		if (photoToLoad.imageView == null)
			return false;
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}

	// Used to display bitmap in the UI thread
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			if (bitmap != null) {
				photoToLoad.imageView.setImageBitmap(bitmap);
/*				photoToLoad.imageView.startAnimation(AnimationUtils
						.loadAnimation(photoToLoad.imageView.getContext(),
								R.anim.td_load_img));*/
			} else {
				try {
					photoToLoad.imageView
							.setImageResource(notAvailableImageResId);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void clearCache() {
		memoryCache.clear();
		fileCache.clear(0);
	}

	public void clearCache(long lastModifiedTimeToNowMillis) {
		memoryCache.clear();
		fileCache.clear(lastModifiedTimeToNowMillis);
	}

	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}

	public Bitmap getBitmap(String url, int width, int height) {
		
		File f = fileCache.getFile(url);

		// from SD cache
		Bitmap b = decodeFile(f, width, height);
		if (b != null)
			return b;

		// from web
		try {
			Util.download2file(url, f);
			return decodeFile(f, width, height);
		} catch (OutOfMemoryError ex) {

			if (ex instanceof OutOfMemoryError)
				memoryCache.clear();
			return null;
		} catch (Exception ex) {
			String test = ex.toString();
			return null;
		}
	}

	private Bitmap decodeFile(File f, int width, int height) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			FileInputStream stream1 = new FileInputStream(f);
			BitmapFactory.decodeStream(stream1, null, o);
			stream1.close();
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			float widthScale = ((float) width) / ((float) width_tmp);
			float heightScale = ((float) height) / ((float) height_tmp);
			float scale = widthScale > heightScale ? heightScale : widthScale;
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.outWidth = (int) (width_tmp * scale);
			o2.outHeight = (int) (height_tmp * scale);
			FileInputStream stream2 = new FileInputStream(f);
			Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
			stream2.close();
			return bitmap;

		} catch (FileNotFoundException e) {
		} catch (Exception e) {
			
		}
		return null;// */
	}

}
