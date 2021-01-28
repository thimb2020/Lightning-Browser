package acr.browser.lightning.loader;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class LifoBlockingDeque<E> extends LinkedBlockingDeque<E> {

    private static final long serialVersionUID = -4854985351588039351L;

    @Override
    public boolean offer(E e) {
        // override to put objects at the front of the list
        remove(e);
        return super.offerFirst(e);
    }

    @Override
    public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
        // override to put objects at the front of the list
        remove(e);
        return super.offerFirst(e, timeout, unit);
    }

    @Override
    public boolean add(E e) {
        // override to put objects at the front of the list
        remove(e);
        return super.offerFirst(e);
    }

    @Override
    public void put(E e) throws InterruptedException {
        // override to put objects at the front of the list
        remove(e);
        super.putFirst(e);
    }
}