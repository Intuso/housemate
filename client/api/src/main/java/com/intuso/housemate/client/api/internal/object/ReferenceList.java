package com.intuso.housemate.client.api.internal.object;

import com.intuso.housemate.client.api.internal.object.view.ListView;
import com.intuso.housemate.client.api.internal.object.view.View;
import com.intuso.utilities.collection.ManagedCollection;

import java.util.Iterator;

/**
 * Created by tomc on 02/05/17.
 */
public class ReferenceList<
        FROM extends Object<?, ?, ?>,
        TO extends Object<?, ?, ?>>
        implements List<TO, ReferenceList<FROM, TO>> {

    private final List<? extends FROM, ?> list;
    private final Converter<? super FROM, ? extends TO> converter;

    public ReferenceList(List<? extends FROM, ?> list, Converter<? super FROM, ? extends TO> converter) {
        this.list = list;
        this.converter = converter;
    }

    @Override
    public Data getData() {
        return list.getData();
    }

    @Override
    public String getObjectClass() {
        return Data.OBJECT_CLASS;
    }

    @Override
    public String getId() {
        return list.getId();
    }

    @Override
    public String getName() {
        return list.getName();
    }

    @Override
    public String getDescription() {
        return list.getDescription();
    }

    @Override
    public ManagedCollection.Registration addObjectListener(Listener<? super TO, ? super ReferenceList<FROM, TO>> listener) {
        return list.addObjectListener(new ReferenceListener(listener));
    }

    @Override
    public TO get(String id) {
        FROM from = list.get(id);
        return from != null ? converter.apply(from) : null;
    }

    @Override
    public TO getByName(String name) {
        FROM from = list.getByName(name);
        return from != null ? converter.apply(from) : null;
    }

    @Override
    public Object<?, ?, ?> getChild(String id) {
        return get(id);
    }

    @Override
    public ListView<?> createView(View.Mode mode) {
        return new ListView<>(mode);
    }

    @Override
    public Tree getTree(ListView<?> view, Tree.ReferenceHandler referenceHandler, Tree.Listener listener, java.util.List<ManagedCollection.Registration> listenerRegistrations) {

        Tree result = new Tree(getData());

        if(view != null && view.getMode() != null) {
            switch (view.getMode()) {
                case ANCESTORS:
                    for (TO to : this)
                        result.getChildren().put(to.getId(), ((Object) to).getTree(to.createView(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    break;
                case CHILDREN:
                    for (TO to : this)
                        result.getChildren().put(to.getId(), ((Object) to).getTree(view.getView(), referenceHandler, listener, listenerRegistrations));
                    break;
                case SELECTION:
                    if (view.getElements() != null) {
                        for (String id : view.getElements()) {
                            TO to = get(id);
                            if (to != null)
                                result.getChildren().put(id, ((Object) to).getTree(view.getView(), referenceHandler, listener, listenerRegistrations));
                        }
                    }
                    break;
            }
        }
        return result;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public ManagedCollection.Registration addObjectListener(Listener<? super TO, ? super ReferenceList<FROM, TO>> listener, boolean callForExistingElements) {
        return list.addObjectListener(new ReferenceListener(listener), callForExistingElements);
    }

    @Override
    public Iterator<TO> iterator() {
        return new ReferenceIterator();
    }

    private class ReferenceListener implements Listener<FROM, List<? extends FROM, ?>> {

        private final Listener<? super TO, ? super ReferenceList<FROM, TO>> listener;

        private ReferenceListener(Listener<? super TO, ? super ReferenceList<FROM, TO>> listener) {
            this.listener = listener;
        }

        @Override
        public void elementAdded(List<? extends FROM, ?> list, FROM element) {
            listener.elementAdded(ReferenceList.this, converter.apply(element));
        }

        @Override
        public void elementRemoved(List<? extends FROM, ?> list, FROM element) {
            listener.elementRemoved(ReferenceList.this, converter.apply(element));
        }
    }

    private class ReferenceIterator implements Iterator<TO> {

        private final Iterator<? extends FROM> iter = list.iterator();

        @Override
        public boolean hasNext() {
            return iter.hasNext();
        }

        @Override
        public TO next() {
            return converter.apply(iter.next());
        }

        @Override
        public void remove() {
            iter.remove();
        }
    }

    public interface Converter<F, T> {
        T apply(F element);
    }
}
