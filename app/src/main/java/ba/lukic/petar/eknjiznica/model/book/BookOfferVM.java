package ba.lukic.petar.eknjiznica.model.book;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

import java.util.List;

public class BookOfferVM implements Parcelable {
    @SerializedName("Id")
    public int Id;
    @SerializedName("BookId")
    public int BookId;
    @SerializedName("Title")
    public String Title;
    @SerializedName("Description")
    public String Description;
    @SerializedName("UserHasBook")
    public boolean UserHasBook;
    @SerializedName("UserRating")
    public int UserRating;
    @SerializedName("AuthorName")
    public String AuthorName;
    @SerializedName("Price")
    public double Price;
    @SerializedName("IsActive")
    public boolean IsActive;
    @SerializedName("ImageUrl")
    public String ImageUrl;
    @SerializedName("AverageRating")
    public double AverageRating;
    @SerializedName("BookState")
    public String BookState;
    @SerializedName("ImageUri")
    public String ImageUri;
    @SerializedName("AddedToFavorites")
    public DateTime AddedToFavorites;
    @SerializedName("Categories")
    public List<CategoryVM> Categories;


    public BookOfferVM(ClientBookVM clientBookVM) {
        Id = clientBookVM.OfferId;
        BookId = clientBookVM.BookId;
        Title = clientBookVM.BookTitle;
        Description = clientBookVM.BookDescription;
        UserHasBook = true;
        UserRating = clientBookVM.UserRating;
        AuthorName = clientBookVM.AuthorName;
        Price = clientBookVM.Price;
        ImageUrl = clientBookVM.FullImageUrl;
        Categories = clientBookVM.Categories;

    }

    protected BookOfferVM(Parcel in) {
        Id = in.readInt();
        BookId = in.readInt();
        Title = in.readString();
        Description = in.readString();
        UserHasBook = in.readByte() != 0;
        UserRating = in.readInt();
        AuthorName = in.readString();
        Price = in.readDouble();
        IsActive = in.readByte() != 0;
        ImageUrl = in.readString();
        AverageRating = in.readDouble();
        BookState = in.readString();
        ImageUri = in.readString();
        Categories = in.createTypedArrayList(CategoryVM.CREATOR);

        long dateTime = in.readLong();
        if (dateTime != 0)
            AddedToFavorites = new DateTime(dateTime);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeInt(BookId);
        dest.writeString(Title);
        dest.writeString(Description);
        dest.writeByte((byte) (UserHasBook ? 1 : 0));
        dest.writeInt(UserRating);
        dest.writeString(AuthorName);
        dest.writeDouble(Price);
        dest.writeByte((byte) (IsActive ? 1 : 0));
        dest.writeString(ImageUrl);
        dest.writeDouble(AverageRating);
        dest.writeString(BookState);
        dest.writeString(ImageUri);
        dest.writeTypedList(Categories);
        if (AddedToFavorites != null)
            dest.writeLong(AddedToFavorites.getMillis());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BookOfferVM> CREATOR = new Creator<BookOfferVM>() {
        @Override
        public BookOfferVM createFromParcel(Parcel in) {
            return new BookOfferVM(in);
        }

        @Override
        public BookOfferVM[] newArray(int size) {
            return new BookOfferVM[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookOfferVM that = (BookOfferVM) o;

        if (Id != that.Id) return false;
        return BookId == that.BookId;
    }

    @Override
    public int hashCode() {
        int result = Id;
        result = 31 * result + BookId;
        return result;
    }
}
