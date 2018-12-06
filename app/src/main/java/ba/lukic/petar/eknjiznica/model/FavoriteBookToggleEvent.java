package ba.lukic.petar.eknjiznica.model;

import ba.lukic.petar.eknjiznica.model.book.BookOfferVM;

public class FavoriteBookToggleEvent {
    public BookOfferVM data;
    public boolean displayNotification;

    public FavoriteBookToggleEvent(BookOfferVM data, boolean displayNotification) {
        this.data = data;
        this.displayNotification = displayNotification;
    }
}
