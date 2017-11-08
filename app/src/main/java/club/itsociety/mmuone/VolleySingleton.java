package club.itsociety.mmuone;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by Timothy on 05/11/2017.
 */

public class VolleySingleton
{
	private static VolleySingleton volleySingletonInstance;
	private RequestQueue requestQueue;
	private ImageLoader imageLoader;
	private static Context context;

	//	We have created an instance of type VolleySingleton and
	//	made the constructor private to apply singleton pattern
	//
	//	To work with volley you construct a request object,
	//	there are different kinds of requests objects that you can use,
	// 	most important ones are StringRequest, JsonObjectRequest, JsonArrayRequest and ImageRequest
	private VolleySingleton(Context context)
	{
		this.context = context;
		requestQueue = getRequestQueue();

		//	we create an ImageLoader object using the
		//	RequestQueue object and a new ImageCache object.
		//	ImageCache is a cache adapter interface.
		//	It is used as an L1 cache before dispatch to Volley.
		//	We add a LruCache object from package java.util.LruCache and
		//	implement the interface methods getBitmap() and putBitmap().
		imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache()
		{
			private final LruCache<String, Bitmap> cache = new LruCache<>(20);

			@Override
			public Bitmap getBitmap(String url)
			{
				return cache.get(url);
			}

			@Override
			public void putBitmap(String url, Bitmap bitmap)
			{
				cache.put(url, bitmap);
			}
		});
	}

	public static synchronized VolleySingleton getInstance(Context context)
	{
		if (volleySingletonInstance == null)
		{
			volleySingletonInstance = new VolleySingleton(context);
		}

		return  volleySingletonInstance;
	}

	//	We have also added getter methods for RequestQueue and ImageLoader.
	//	In the getRequestQueue() method we check whether the object already exists,
	//	if not we use the convenience method Volley.newRequestQueue(),
	//	pass in the application context and return the RequestQueue reference.
	public RequestQueue getRequestQueue()
	{
		if (requestQueue == null)
		{
			//	getApplicationContext() is key, it keeps you from leaking the
			//	Activity or BroadcastReceiver if someone passes one in
			requestQueue = Volley.newRequestQueue(context);
		}

		return requestQueue;
	}

	//	The method addToRequestQueue() takes in a generic Type and adds it to request queue.
	public <T> void addToRequestQueue(Request<T> request, String tag)
	{
		request.setTag(tag);
		getRequestQueue().add(request);
	}

	protected ImageLoader getImageLoader()
	{
		return imageLoader;
	}

	//	cancelPendingRequests() is used to cancel a request using a tag.
	public void cancelPendingRequest(Object tag)
	{
		if (requestQueue != null)
		{
			requestQueue.cancelAll(tag);
		}
	}
}
