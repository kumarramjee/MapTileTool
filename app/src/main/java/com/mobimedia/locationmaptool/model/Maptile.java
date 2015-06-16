package com.mobimedia.locationmaptool.model;
import android.os.Parcel;
import android.os.Parcelable;
public class Maptile implements Parcelable {
	public String imageName;
	public int imageId;
	public String toprightlatlong;
	public String topleftlatlong;
	public String bottomleftlatlong;
	public String bottomnrightlatlong;
    public Maptile(){
    	
    }
	private Maptile(Parcel in) {
		imageName = in.readString();
		imageId = in.readInt();
		bottomleftlatlong = in.readString();
		toprightlatlong = in.readString();
		topleftlatlong = in.readString();
		bottomnrightlatlong = in.readString();

	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public String getBottomleftlatlong() {
		return bottomleftlatlong;
	}

	public void setBottomleftlatlong(String bottomleftlatlong) {
		this.bottomleftlatlong = bottomleftlatlong;
	}

	public String getToprightlatlong() {
		return toprightlatlong;
	}

	public void setToprightlatlong(String toprightlatlong) {
		this.toprightlatlong = toprightlatlong;
	}

	public String getTopleftlatlong() {
		return topleftlatlong;
	}

	public void setTopleftlatlong(String topleftlatlong) {
		this.topleftlatlong = topleftlatlong;
	}

	public String getBottomnrightlatlong() {
		return bottomnrightlatlong;
	}

	public void setBottomnrightlatlong(String bottomnrightlatlong) {
		this.bottomnrightlatlong = bottomnrightlatlong;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(imageName);
		dest.writeInt(imageId);
		dest.writeString(bottomleftlatlong);
		dest.writeString(toprightlatlong);
		dest.writeString(topleftlatlong);
		dest.writeString(bottomnrightlatlong);
	}
	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Maptile createFromParcel(Parcel in) {
            return new Maptile(in);
        }
        public Maptile[] newArray(int size) {
            return new Maptile[size];
        }
    };
}
