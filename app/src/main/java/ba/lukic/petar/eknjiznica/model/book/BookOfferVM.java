package ba.lukic.petar.eknjiznica.model.book;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

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
    @SerializedName("Price ")
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
}
