package ba.lukic.petar.eknjiznica.model.user;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

public class ClientsDetailsModel implements Parcelable {

    public String Id;
    public String FirstName ;
    public String LastName ;
    public String UserName ;
    public String Email ;
    public String PhoneNumber ;
    public double AccountBalance ;
    public boolean IsActive ;


    public ClientsDetailsModel(){

    }
    protected ClientsDetailsModel(Parcel in) {
        Id = in.readString();
        FirstName = in.readString();
        LastName = in.readString();
        UserName = in.readString();
        Email = in.readString();
        PhoneNumber = in.readString();
        AccountBalance = in.readDouble();
        IsActive = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Id);
        dest.writeString(FirstName);
        dest.writeString(LastName);
        dest.writeString(UserName);
        dest.writeString(Email);
        dest.writeString(PhoneNumber);
        dest.writeDouble(AccountBalance);
        dest.writeByte((byte) (IsActive ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ClientsDetailsModel> CREATOR = new Creator<ClientsDetailsModel>() {
        @Override
        public ClientsDetailsModel createFromParcel(Parcel in) {
            return new ClientsDetailsModel(in);
        }

        @Override
        public ClientsDetailsModel[] newArray(int size) {
            return new ClientsDetailsModel[size];
        }
    };
}
