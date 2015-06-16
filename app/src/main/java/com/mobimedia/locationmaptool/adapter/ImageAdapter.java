package com.mobimedia.locationmaptool.adapter;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobimedia.locationmaptool.R;
import com.mobimedia.locationmaptool.R.id;
import com.mobimedia.locationmaptool.R.layout;
import com.mobimedia.locationmaptool.helper.MobiUtil;
import com.mobimedia.locationmaptool.model.Maptile;
public class ImageAdapter extends BaseAdapter 
{
	 private Context context;
	 private List<Maptile> mListMapTiles;
	 private MobiUtil mMobiUtil;
	public ImageAdapter(Activity context,List<Maptile> listMapTiles)
	{
         this.context=context;
         mListMapTiles = listMapTiles;
         mMobiUtil = new MobiUtil();
	}
	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    View rowView = inflater.inflate(R.layout.row_grid, parent, false);
		    TextView textView = (TextView) rowView.findViewById(R.id.item_text);
		    Maptile maptile = getItem(position);
		    textView.setText((maptile.imageName).substring(0,10));
		    
		    ImageView imageView = (ImageView) rowView.findViewById(R.id.item_image);
		    Bitmap bmp = mMobiUtil.getBitmapFromAssets(maptile,context);
		    imageView.setImageBitmap(bmp);
		    		    imageView.setTag(position);
		     		 
            TextView topleftlng=(TextView)rowView.findViewById(R.id.edittopleftlong);
		    topleftlng.setText(maptile.topleftlatlong);
		    
	        TextView toprtlng=(TextView)rowView.findViewById(R.id.edittoprightlong);
		    toprtlng.setText(maptile.toprightlatlong);
		 
		    TextView btmleft=(TextView)rowView.findViewById(R.id.editbottomleftlong);
		    btmleft.setText(maptile.bottomleftlatlong);
		    
		    TextView btmrt=(TextView)rowView.findViewById(R.id.editbottomrightlong);
	        btmrt.setText(maptile.bottomnrightlatlong);
		           	 	return rowView;
		           	 
		           	 	
		           	 
		}
	@Override
	public int getCount() {
		return mListMapTiles.size();
	}
	@Override
	public Maptile getItem(int position) {
		return mListMapTiles.get(position);
	}
	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	
}




































//LayoutInflater inflater=(context).getLayoutInflater();
		/*ImageView imageView = new ImageView(mContext);
      imageView.setImageResource(mThumbIds[position]);
      TextView text=new TextView(mContext);
      text.setText(Imagename[position]);
      
      TextView text=(TextView)convertView.findViewById(R.id.item_text);
      text.setText(Imagename[position]);
      
      imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
      imageView.setLayoutParams(new GridView.LayoutParams(90, 90));
      
      
      return imageView;
      */
      
      /*
      LayoutInflater inflater=context.getLayoutInflater();
View rowView=inflater.inflate(R.layout.mylist, null,true);

TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);

txtTitle.setText(itemname[position]);
imageView.setImageResource(imgid[position]);
extratxt.setText("Description "+itemname[position]);
return rowView;

      
      */
      