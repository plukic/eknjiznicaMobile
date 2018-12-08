package ba.lukic.petar.eknjiznica.model.book;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

import java.util.List;

import ba.lukic.petar.eknjiznica.model.category.CategoryVM;

public class ClientBookVM implements Parcelable {
    @SerializedName("Id")
    public int Id;
    @SerializedName("BookTitle")
    public String BookTitle;
    @SerializedName("AuthorName")
    public String AuthorName;
    @SerializedName("ClientName")
    public String ClientName;
    @SerializedName("BookDescription")
    public String BookDescription;
    @SerializedName("Price")
    public double Price;
    @SerializedName("BuyDate")
    public DateTime BuyDate;
    @SerializedName("BookReleaseDate")
    public DateTime BookReleaseDate;
    @SerializedName("Categories")
    public List<CategoryVM> Categories;
    @SerializedName("ImageUrl")
    public String ImageUrl;
    @SerializedName("TransactionId")
    public int TransactionId;
    @SerializedName("OfferId")
    public int OfferId;
    @SerializedName("UserRating")
    public int UserRating;
    @SerializedName("BookId")
    public int BookId;
    public String FullImageUrl;
    protected ClientBookVM(Parcel in) {
        Id = in.readInt();
        BookTitle = in.readString();
        AuthorName = in.readString();
        ClientName = in.readString();
        BookDescription = in.readString();
        Price = in.readDouble();
        Categories = in.createTypedArrayList(CategoryVM.CREATOR);
        ImageUrl = in.readString();
        TransactionId = in.readInt();
        OfferId = in.readInt();
        UserRating = in.readInt();
        BookId = in.readInt();

        BuyDate = new DateTime(in.readLong());
        BookReleaseDate = new DateTime(in.readLong());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(BookTitle);
        dest.writeString(AuthorName);
        dest.writeString(ClientName);
        dest.writeString(BookDescription);
        dest.writeDouble(Price);
        dest.writeTypedList(Categories);
        dest.writeString(ImageUrl);
        dest.writeInt(TransactionId);
        dest.writeInt(OfferId);
        dest.writeInt(UserRating);
        dest.writeInt(BookId);
        dest.writeLong(BuyDate.getMillis());
        dest.writeLong(BookReleaseDate.getMillis());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ClientBookVM> CREATOR = new Creator<ClientBookVM>() {
        @Override
        public ClientBookVM createFromParcel(Parcel in) {
            return new ClientBookVM(in);
        }

        @Override
        public ClientBookVM[] newArray(int size) {
            return new ClientBookVM[size];
        }
    };
}
