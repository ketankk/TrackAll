package in.kuari.trackall.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.media.Image;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by root on 1/24/16.
 */
public class MLRoundedImageView extends ImageView {

    Drawable drawable;
    Bitmap bitmap;
    Canvas c;

    public MLRoundedImageView(Context context) {
        super(context);
        drawable = getDrawable();
        if (drawable instanceof BitmapDrawable) {

        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            c = new Canvas(bitmap);
        }
    }

    public MLRoundedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        drawable = getDrawable();
        if (drawable instanceof BitmapDrawable) {

        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            c = new Canvas(bitmap);
        }
    }

    public MLRoundedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        drawable = getDrawable();
        if (drawable instanceof BitmapDrawable) {

        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            c = new Canvas(bitmap);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Drawable drawable = getDrawable();

        if (drawable == null) {
            return;
        }

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }

        Bitmap b;
        if (drawable instanceof BitmapDrawable) {
            b = ((BitmapDrawable) drawable).getBitmap();
        } else {
            drawable.setBounds(0, 0, c.getWidth(), c.getHeight());
            drawable.draw(c);
            b = bitmap;
        }

        Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);

        int w = getWidth(), h = getHeight();

        Bitmap roundBitmap = getCroppedBitmap(bitmap, w);
        canvas.drawBitmap(roundBitmap, 0, 0, null);

    }

    public static Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
        Bitmap sbmp;

        if (bmp.getWidth() != radius || bmp.getHeight() != radius) {
            float smallest = Math.min(bmp.getWidth(), bmp.getHeight());
            float factor = smallest / radius;
            sbmp = Bitmap.createScaledBitmap(bmp,
                    (int) (bmp.getWidth() / factor),
                    (int) (bmp.getHeight() / factor), false);
        } else {
            sbmp = bmp;
        }

        Bitmap output = Bitmap.createBitmap(radius, radius, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xffa19774;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, radius, radius);

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(radius / 2 + 0.7f, radius / 2 + 0.7f,
                radius / 2 + 0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(sbmp, rect, rect, paint);

        return output;
    }
}
/*RoundedImageView riv = new RoundedImageView(context);
riv.setScaleType(ScaleType.CENTER_CROP);
riv.setCornerRadius((float) 10);
riv.setBorderWidth((float) 2);
riv.setBorderColor(Color.DKGRAY);
riv.mutateBackground(true);
riv.setImageDrawable(drawable);
riv.setBackground(backgroundDrawable);
riv.setOval(true);
riv.setTileModeX(Shader.TileMode.REPEAT);
riv.setTileModeY(Shader.TileMode.REPEAT);

Or make a Transformation for Picasso:

Transformation transformation = new RoundedTransformationBuilder()
          .borderColor(Color.BLACK)
          .borderWidthDp(3)
          .cornerRadiusDp(30)
          .oval(false)
          .build();

Picasso.with(context)
    .load(url)
    .fit()
    .transform(transformation)
    .into(imageView);

*/

/*void readSMS(){
        Uri uri=Uri.parse("content://sms/inbox");
        Cursor c=context.getContentResolver().query(uri,null,null,null,null);
//        Log.d("outside","cusrsor");

        while (c.moveToNext()){
           // Log.d("inside","cusrsor");
           unilist.add(c.getString(c.getColumnIndex("body")));
            list.add(c.getString(c.getColumnIndex("body")));

        }
    }*/