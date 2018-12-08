package ba.lukic.petar.eknjiznica.model.book;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CategoryVM implements Parcelable {
    @SerializedName("Id")
    public int Id;
    @SerializedName("CategoryName")
    public String CategoryName;
    @SerializedName("NumberOfBooks")
    public int NumberOfBooks;
    @SerializedName("IsActive")
    public boolean IsActive;

    protected CategoryVM(Parcel in) {
        Id = in.readInt();
        CategoryName = in.readString();
        NumberOfBooks = in.readInt();
        IsActive = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(CategoryName);
        dest.writeInt(NumberOfBooks);
        dest.writeByte((byte) (IsActive ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CategoryVM> CREATOR = new Creator<CategoryVM>() {
        @Override
        public CategoryVM createFromParcel(Parcel in) {
            return new CategoryVM(in);
        }

        @Override
        public CategoryVM[] newArray(int size) {
            return new CategoryVM[size];
        }
    };
}
