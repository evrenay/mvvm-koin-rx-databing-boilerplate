package com.news.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.*
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Parcelable
import android.provider.MediaStore
import android.provider.Settings
import androidx.annotation.DrawableRes
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.FragmentManager
import androidx.core.graphics.drawable.DrawableCompat
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import android.text.Html
import android.text.InputFilter
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.TimePicker
import com.news.R
import com.news.base.BaseFragment
import com.news.base.BaseFragmentPagerAdapter
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/*
* BottomNavigationView
* */
@SuppressLint("RestrictedApi")
fun BottomNavigationView.disableShiftMode(){
    val menuView = this.getChildAt(0) as BottomNavigationMenuView
    try {
        val shiftingMode = menuView.javaClass.getDeclaredField("mShiftingMode")
        shiftingMode.isAccessible = true
        shiftingMode.setBoolean(menuView, false)
        shiftingMode.isAccessible = false
        for (i in 0..menuView.childCount - 1) {
            val item = menuView.getChildAt(i) as BottomNavigationItemView

            //item.setShiftingMode(false)
            item.setPadding(0, 10.convertDpToPixel(this.context), 0, 0);
            // set once again checked value, so view will be updated

            item.itemData?.let { item.setChecked(it.isChecked) }
        }
    } catch (e: NoSuchFieldException) {
        Log.e("BNVHelper", "Unable to get shift mode field", e)
    } catch (e: IllegalAccessException) {
        Log.e("BNVHelper", "Unable to change value of shift mode", e)
    }
}
/*
* */



/*
*
* String
* */

fun String.isValidEmail() : Boolean{
    return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

}


fun String.fileName():String{
    return this.substring( this.lastIndexOf('/')+1, this.length);
}

fun String.isUrl() : Boolean{
    return this.startsWith("http")
}


fun String.toDate(format:String) : Date{
    return SimpleDateFormat(format).parse(this)
}




fun String.getBitmapFromString() : Bitmap?{
    if(File(this).exists()){
        return File(this).decodeFileToBitmap()
    }
    return null
}

fun String.toOutputDateFormat(originalFormat: String, outputFromat: String): String {

    val formatter = SimpleDateFormat(originalFormat, Locale.US)
    var date: Date? = null
    try {
        date = formatter.parse(this)
        date = Date(date!!.time /*+ SERVER_DIFFERENCE * HOUR*/)

        val dateFormat = SimpleDateFormat(outputFromat, Locale("TR"))

        return dateFormat.format(date)

    } catch (e: ParseException) {
        e.printStackTrace()
        return ""
    }

}


fun Date.toOutputDateFormat(outputFromat: String): String {


    try {
        val dateFormat = SimpleDateFormat(outputFromat, Locale("TR"))
        return dateFormat.format(this)
    } catch (e: ParseException) {
        e.printStackTrace()
        return ""
    }

}


/**/




/*
* ActivityDto
* */





fun <T>AppCompatActivity.startNewActivity(type:Class<T>,passItems :HashMap<String,Serializable>? = null){
    val intent = Intent(this,type)
    if (passItems != null) {
        for ((key, value) in passItems) {
            intent.putExtra(key, value)
        }
    }
    this.startActivity(intent)
}


fun <T> AppCompatActivity.startActivityForResult(toClass: Class<T>, passItems: HashMap<String, Serializable>?, requestCode: Int) {
    val intent = Intent(this, toClass)
    if (passItems != null) {
        for ((key, value) in passItems) {
            intent.putExtra(key, value)
        }
    }
    this.startActivityForResult(intent, requestCode)
}

fun <T> AppCompatActivity.startActivityClearAllActivities( toClass: Class<T>, bundles: HashMap<String, Serializable>?) {
    val intent = Intent(this, toClass)
    if (bundles != null) {
        for ((key, value) in bundles) {

            intent.putExtra(key, value)
        }
    }
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    this.startActivity(intent)
}


fun <T>AppCompatActivity.startNewActivityParcelableExtra(type:Class<T>,passItems :HashMap<String,Parcelable>? = null){
    val intent = Intent(this,type)
    if (passItems != null) {
        for ((key, value) in passItems) {
            intent.putExtra(key, value)
        }
    }
    this.startActivity(intent)
}


fun AppCompatActivity.startShareActivity(content : String){
    val sendIntent = Intent()
    sendIntent.action = Intent.ACTION_SEND
    sendIntent.putExtra(Intent.EXTRA_TEXT,content)
    sendIntent.type = "text/plain"
    this.startActivity(Intent.createChooser(sendIntent, ""))
}

fun AppCompatActivity.hideKeyboard() {
    val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    var view = this.getCurrentFocus()
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
        view = View(this);
    }
    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
}



/*storage*/
fun AppCompatActivity.getExternalStorageDirectoryFile(fileName:String,extension:String) : File{
    var path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES+"/"+this.getString(R.string.app_name))
    path.mkdirs()
    return File("$path/$fileName.$extension")
}

fun AppCompatActivity.getInternalStorageDirectoryFile(directoryName:String,fileName:String,extension:String) : File{
    var path = this.getDir(directoryName,Context.MODE_PRIVATE)
    path.mkdirs()
    return File("$path/$fileName.$extension")
}


fun AppCompatActivity.getAllImageDirectories() : Array<String>?{
    var u = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    var projection = arrayOf(MediaStore.Images.ImageColumns.DATA)
    var c : Cursor? = null
    var dirList = TreeSet<String>()
    var directories:Array<String>? = null

    if (u != null) {
        c = this.managedQuery(u, projection, null, null,  MediaStore.Images.ImageColumns.DATE_TAKEN  +" DESC")
    }

    if ((c != null) && (c.moveToFirst())) {
        do {
            var tempDir = c.getString(0)
            tempDir = tempDir.substring(0, tempDir.lastIndexOf("/"))
            try{
                dirList.add(tempDir)
            }
            catch(e:Exception)
            {}
        }
        while (c.moveToNext())
        directories = Array(dirList.size){"n = $it"}
        dirList.toArray(directories)
    }
    return directories
}


///

/**/



/*
*
* Float
* */


/**
 * This method converts dp unit to equivalent pixels, depending on device density.
 *
 * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
 * @param context Context to get resources and device specific display metrics
 * @return A float value to represent px equivalent to dp depending on device density
 */
fun Int.convertDpToPixel(context: Context): Int {
    val resources = context.getResources()
    val metrics = resources.getDisplayMetrics()
    return this * (metrics.densityDpi  / DisplayMetrics.DENSITY_DEFAULT)
}

/**
 * This method converts device specific pixels to density independent pixels.
 *
 * @param px A value in px (pixels) unit. Which we need to convert into db
 * @param context Context to get resources and device specific display metrics
 * @return A float value to represent dp equivalent to px value
 */
fun Int.convertPixelsToDp(context: Context): Int {
    val resources = context.getResources()
    val metrics = resources.getDisplayMetrics()
    return (this / (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)).toInt()
}


/**/




/*viewpager*/

fun ViewPager.setupViewPager(fragmentManager: FragmentManager, fragments: Map<String, BaseFragment<*, *>>) {
    this.offscreenPageLimit = fragments.size - 1
    val adapter = BaseFragmentPagerAdapter(fragmentManager)
    for ((key, value) in fragments) {
        adapter.addFrag(value, key)
    }
    this.adapter = adapter
}




/**/




/*edittext*/


fun EditText.setMaxLength(maxLength : Int){
    val fArray = arrayOfNulls<InputFilter>(1)
    fArray[0] = InputFilter.LengthFilter(maxLength)
    this.filters = fArray
}


fun EditText.openDateTimePickerDialog(dateFormat:String,hourFormat:String,onDateSet : (dateStr : String) ->Unit,context: Context,selectedDateText:String?=null,selectedDateFormat : String?=null){
    openDatePickerDialog(dateFormat,{
        dateStr ->
        openTimePickerDialog(hourFormat,{
            hourStr->
            val output = dateStr+" "+hourStr
            onDateSet(output)

        },"",context)

    },context,selectedDateText,selectedDateFormat)
}

fun EditText.openDatePickerDialog(dateFormat:String,onDateSet : (dateStr : String) ->Unit,context: Context,selectedDateText:String?=null,selectedDateFormat : String?=null){
    val calendar = Calendar.getInstance()
    val successListener = DatePickerDialog.OnDateSetListener(function = {
        datePicker, year, monthOfYear, dayOfMonth ->

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.US)
        val dateStr = simpleDateFormat.format(calendar.time)
        onDateSet(dateStr)
    })
    val dialog = DatePickerDialog(context,successListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH))

    if(!selectedDateText.isNullOrEmpty() && !selectedDateFormat.isNullOrEmpty()){
        val selectedDate = selectedDateText!!.toDate(selectedDateFormat!!)
        val selectedDateCalendar  =  Calendar.getInstance()
        selectedDateCalendar.setTime(selectedDate)
        dialog.updateDate(selectedDateCalendar.get(Calendar.YEAR),selectedDateCalendar.get(Calendar.MONTH),selectedDateCalendar.get(Calendar.DAY_OF_MONTH))
    }
    dialog.show()
}



fun EditText.openTimePickerDialog(dateFormat:String,onDateSet : (dateStr : String) ->Unit,title:String,context: Context,selectedDateText:String?=null,selectedDateFormat : String?=null){
    val mcurrentTime = Calendar.getInstance();
    val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
    val minute = mcurrentTime.get(Calendar.MINUTE)
    var timePicker : TimePickerDialog? = null
    timePicker = TimePickerDialog(context, object:TimePickerDialog.OnTimeSetListener {
        override fun onTimeSet(p0: TimePicker?, selectedHour: Int, selectedMinute: Int) {
            //this@openTimePickerDialog.setText(String.format("%02d:%02d", selectedHour, selectedMinute))
            onDateSet(String.format("%02d:%02d", selectedHour, selectedMinute))
        }

    }, hour, minute, true)
    timePicker.setTitle(title)


    if(!selectedDateText.isNullOrEmpty() && !selectedDateFormat.isNullOrEmpty()){
        val selectedDate = selectedDateText!!.toDate(selectedDateFormat!!)
        val selectedDateCalendar  =  Calendar.getInstance()
        selectedDateCalendar.setTime(selectedDate)
        timePicker.updateTime(selectedDateCalendar.get(Calendar.HOUR_OF_DAY),selectedDateCalendar.get(Calendar.MINUTE))
    }
    timePicker.show()
}



/**/




/*bitmap*/



fun Bitmap.getBitmapAsApptivityFormat(path: String) : Bitmap{
    return this.centerCrop()
            .resizeBitmap(UPLOAD_IMAGE_SIZE,UPLOAD_IMAGE_SIZE)
            .controlImageOrientation(path)!!
}

fun Bitmap.resizeBitmap(width : Int,height:Int) :Bitmap{
    return Bitmap.createScaledBitmap(this, width, height, false)
}


fun Bitmap.centerCrop() : Bitmap{
    var bitmap : Bitmap ?= null
    if (this.width >= this.height){
        bitmap = Bitmap.createBitmap(
                this,
                this.width/2 - this.height/2,
                0,
                this.getHeight(),
                this.getHeight()
        )

    }else{

        bitmap = Bitmap.createBitmap(
                this,
                0,
                this.getHeight()/2 - this.getWidth()/2,
                this.getWidth(),
                this.getWidth()
        )
    }
    return bitmap
}


fun Bitmap.controlImageOrientation(uri : Uri,context: Context) : Bitmap?{
    var ei = context.getContentResolver().openInputStream(uri)?.let {
        ExifInterface(
            it
    )
    }
    var orientation = ei?.getAttributeInt(
        ExifInterface.TAG_ORIENTATION,
        ExifInterface.ORIENTATION_UNDEFINED);

    var rotatedBitmap :Bitmap? = null
    when(orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 ->{
            rotatedBitmap = this.rotateImage( 90f)
        }
        ExifInterface.ORIENTATION_ROTATE_180->
            rotatedBitmap = this.rotateImage( 180f)

        ExifInterface.ORIENTATION_ROTATE_270-> rotatedBitmap = this.rotateImage( 270f)
        ExifInterface.ORIENTATION_NORMAL->{}
        else -> rotatedBitmap = this
    }

    return rotatedBitmap
}

fun Bitmap.controlImageOrientation(path : String) : Bitmap?{
    var ei = ExifInterface(path)
    var orientation = ei.getAttributeInt(
        ExifInterface.TAG_ORIENTATION,
                                         ExifInterface.ORIENTATION_UNDEFINED);

    var rotatedBitmap :Bitmap? = null
    when(orientation) {
         ExifInterface.ORIENTATION_ROTATE_90 ->{
             rotatedBitmap = this.rotateImage( 90f)
         }
        ExifInterface.ORIENTATION_ROTATE_180->
            rotatedBitmap = this.rotateImage( 180f)

        ExifInterface.ORIENTATION_ROTATE_270-> rotatedBitmap = this.rotateImage( 270f)
        ExifInterface.ORIENTATION_NORMAL->{}
        else -> rotatedBitmap = this
    }

    return rotatedBitmap
}

fun Bitmap.rotateImage(angle: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(angle)
    return Bitmap.createBitmap(this, 0, 0, this.width, this.height,
            matrix, true)
}


@Throws(IOException::class)
fun Bitmap.toFile(context: Context,fileName: String): File {
    val f = File(context.cacheDir, "1.png")
    f.createNewFile()
    val bos = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos)
    val bitmapdata = bos.toByteArray()

    val fos = FileOutputStream(f)
    fos.write(bitmapdata)
    fos.flush()
    fos.close()

    return f
}


fun Bitmap.rotateBitmap(angle: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(angle)
    return Bitmap.createBitmap(this, 0, 0, this.width, this.height, matrix, true)
}




fun Bitmap.storeImageBitmap(file : File?, quality:Int, format:Bitmap.CompressFormat) : String?{
    if (file == null) {
        Log.d("","Error creating media file, check storage permissions: ")
        return null;
    }


    try {
        var fos = FileOutputStream(file)
        this.compress(format, quality, fos);
        fos.close()
        return file.path

    } catch (e:FileNotFoundException) {
        Log.d("", "File not found: " + e.message);
    } catch (e:IOException) {
        Log.d("", "Error accessing file: " + e.message);
    }

    return null

}

//








/*imageview*/


fun ImageView.displayImageOriginal(ctx: Context, @DrawableRes drawable: Int) {
    try {
        /*Glide.with(ctx).load(drawable)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(this)*/
    } catch (e: Exception) {
    }

}



fun ImageView.setImageFromFile(file:File){
    if(file.exists()){
        var bitmap = BitmapFactory.decodeFile(file.getAbsolutePath())
        this.setImageBitmap(bitmap)
    }
}

//


//file




fun File.getBitmapFromFile() : Bitmap?{
    if(this.exists()){
        val options = BitmapFactory.Options()
        options.inSampleSize = 4
        return BitmapFactory.decodeFile(this.getAbsolutePath(),options)
    }
    return null
}

fun File.decodeFileToBitmap(): Bitmap? {
    try {
        // Decode image size
        val o = BitmapFactory.Options()
        o.inJustDecodeBounds = true
        BitmapFactory.decodeStream(FileInputStream(this), null, o)

        // The new size we want to scale to
        val REQUIRED_SIZE = 300

        // Find the correct scale value. It should be the power of 2.
        var scale = 1
        while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE) {
            scale *= 2
        }

        // Decode with inSampleSize
        val o2 = BitmapFactory.Options()
        o2.inSampleSize = scale
        return BitmapFactory.decodeStream(FileInputStream(this), null, o2)
    } catch (e: FileNotFoundException) {
    }

    return null
}





fun getImagesByDirectory(directoryName: String) : ArrayList<String>?{
    var array = arrayListOf<String>()

    var imageDir = File(directoryName)
    var imageList = imageDir.listFiles();
    if(imageList == null)
        return null
    for (imagePath in imageList) {
        try {
            if(imagePath.isDirectory())
            {
                imageList = imagePath.listFiles();

            }
            if ( imagePath.getName().contains(".jpg")|| imagePath.getName().contains(".JPG")
                    || imagePath.getName().contains(".jpeg")|| imagePath.getName().contains(".JPEG")
                    || imagePath.getName().contains(".png") || imagePath.getName().contains(".PNG")
                    || imagePath.getName().contains(".gif") || imagePath.getName().contains(".GIF")
                    || imagePath.getName().contains(".bmp") || imagePath.getName().contains(".BMP")
            )
            {
                var path= imagePath.getAbsolutePath();
                array.add(path)
            }
        }
        catch (e:Exception) {
            e.printStackTrace();
        }
    }

    if(array.size>0){
        fun selector(directory:String) : Long = File(directory).lastModified()
        array.sortBy {
            item->
            selector(item)
        }
    }


    return array
}



//



/*Context*/





/**/



fun retriveVideoFrameFromVideo(videoPath:String,onSuccess: (b:Bitmap)->Unit,onError: (e:String)->Unit){

    Observable.defer<Bitmap> {
        var bitmap:Bitmap? = null
        var mediaMetadataRetriever:MediaMetadataRetriever? = null
        try
        {
            mediaMetadataRetriever = MediaMetadataRetriever()
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath,HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST);
        }
        catch (e:Exception)
        {
            e.printStackTrace();
            throw Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)"+ e.message);
        }
        finally
        {
            if (mediaMetadataRetriever != null)
            {
                mediaMetadataRetriever.release();
            }
        }
        Observable.just<Bitmap>(bitmap)
    }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(object : Observer<Bitmap> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: Bitmap) {
                    onSuccess(t)
                }

                override fun onError(e: Throwable) {
                    if(e.message!=null) onError(e.message!!)
                }

                override fun onComplete() {}
            })
}

fun AppCompatActivity.openGoogleMaps(longtitude:Double,latitude:Double) {
    val gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=$latitude,$longtitude&travelmode=driving")
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    this.startActivity(mapIntent)


}



fun View.setDrawableWithTint(drawableId:Int,tinColorId:Int){
    val drawable = resources.getDrawable(drawableId)
    DrawableCompat.setTint(drawable,resources.getColor(tinColorId))
    this.setBackgroundDrawable(drawable)
}












/*
* WebView
* */


fun WebView.showSvgFile(svgAssetPath : String){
    var htmlTemplate = "<html><head></head><body><img src=\"file:///android_asset/$svgAssetPath\"></body></html>"
    this.setBackgroundColor(Color.TRANSPARENT);
    this.loadDataWithBaseURL(null, htmlTemplate, "text/html", "utf-8",null);

}


///



/*
* AlertDialog
* */

fun AppCompatActivity.showAlertDialog(viewId:Int,delay:Long){
    val dialogBuilder = AlertDialog.Builder(this)
    val inflater = this.getLayoutInflater()
    var dialogView = inflater.inflate(viewId, null)
    dialogBuilder.setView(dialogView)
    var alertDialog = dialogBuilder.create()
    alertDialog.show()
    Handler().postDelayed({
        alertDialog.cancel()
    },delay)

}


//



/**
 * Uri
 * */


fun Uri.toBitmap(context: Context) : Bitmap{
    return MediaStore.Images.Media.getBitmap(context.getContentResolver(), this);

}
fun String.deleteFileRx() : Observable<Boolean>{
    return Observable.create<Boolean> { emitter ->
        var result = true
        try {
            File(this).delete()
        }
        catch (e:Error){
            result = false
        }
        emitter.onNext(result)
        emitter.onComplete()
    }
}


fun String.stripHtml() : String {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
       return Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY).toString()
    } else {
       return Html.fromHtml(this).toString()
    }
}

//



/*
* Intent
* */





//



fun Context.showPopUpMenu(view:View,menuId:Int,onItemClicked : (id:Int)->Unit){
    val popupMenu = PopupMenu(this, view)

    popupMenu.setOnMenuItemClickListener { item ->
        onItemClicked(item.itemId)
        true
    }
    popupMenu.inflate(menuId)

    try {
        val method = popupMenu.getMenu().javaClass.getDeclaredMethod("setOptionalIconsVisible", Boolean::class.javaPrimitiveType)
        method.setAccessible(true)
        method.invoke(popupMenu.getMenu(), true)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    popupMenu.show()
}


fun  Context.isLocationEnabled() : Boolean {
    var locationMode = 0;
    var locationProviders : String?=null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        try {
            locationMode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
        } catch (e:Settings.SettingNotFoundException ) {
            e.printStackTrace();
        }
        return locationMode != Settings.Secure.LOCATION_MODE_OFF;
    } else {
        locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        return !TextUtils.isEmpty(locationProviders);
    }
}



/*
* Tablayout
* */

fun TabLayout.changeTabsFont(context: Context, assetFontPath : String) {
    val vg = this.getChildAt(0) as ViewGroup
    val tabsCount = vg.childCount
    val tf = Typeface.createFromAsset(context.assets, assetFontPath)
    for (j in 0 until tabsCount) {
        val vgTab = vg.getChildAt(j) as ViewGroup
        val tabChildsCount = vgTab.childCount
        for (i in 0 until tabChildsCount) {
            val tabViewChild = vgTab.getChildAt(i)
            if (tabViewChild is TextView) {
                tabViewChild.typeface = tf
            }
        }
    }
}

//







