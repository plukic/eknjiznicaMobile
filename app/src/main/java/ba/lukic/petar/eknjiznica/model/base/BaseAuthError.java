package ba.lukic.petar.eknjiznica.model.base;

import com.google.gson.annotations.SerializedName;

public class BaseAuthError {
    @SerializedName("error")
    public String error;
    @SerializedName("error_description")
    public String error_description;
}
