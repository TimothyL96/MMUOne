package club.itsociety.mmuone;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by Timothy on 07/11/2017.
 */

public class VolleyActivity
{
	private Map<String, String> params = new HashMap<>();

	public void setParams(Map<String, String> params)
	{
		this.params = params;
	}

	//	Volley request for String Object
	public void volleyStringRequest(String url, final Context context)
	{
		//	The REQUEST_TAG is used to cancel the request
		final String requestTag = "volleyStringRequest";

		//	We have created a new StringRequest Object, the constructor takes in three arguments.
		//
		//	Url: the URL for the network request.
		//	Listener Object: anonymous inner type, an implementation of Response.Listener(),
		//	It has an onResponse method which will receive the string from the web.
		//	ErrorListener Object: anonymous inner type , an implementation of
		//	onErrorResponse(VolleyError err) will get an instance of object of Volley Error
		//
		//	In the onResponse() method, we are logging the output to LogCat and also
		//	showing the response recieved in an AlertDialog.
		//	In the onErrorResponse() method, we are simply logging the error message to LogCat.
		StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>()
				{
					@Override
					public void onResponse(String response)
					{
						Log.d(requestTag, response);
						LayoutInflater li = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
						View showDialogView = li.inflate(R.layout.activity_register, null);
						TextView textView = showDialogView.findViewById(R.id.textView);
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
						alertDialogBuilder.setView(showDialogView);
						alertDialogBuilder
								.setPositiveButton("OK", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
									}
								})
								.setCancelable(false)
								.create();
						textView.setText(response);
						alertDialogBuilder.show();
					}
				},
				new Response.ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error)
					{
						VolleyLog.d(requestTag, "Error: " + error.getMessage());
					}
				})
		{
			@Override
			protected Map<String, String> getParams()
			{
				return params;
			}
		};

		//	We need to add the request to a queue,
		//	We get the instance of the RequestQueue from VolleySingleton and
		//	add our request to the queue.
		//	Adding String request to request queue
		VolleySingleton.getInstance(context).addToRequestQueue(stringRequest, requestTag);
	}

	//	Volley request for JSON Object
	public void volleyJsonObjectRequest(String url, final Context context)
	{
		final String requestTag = "volleyJsonObjectRequest";
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
				new Response.Listener<JSONObject>()
				{
					@Override
					public void onResponse(JSONObject response)
					{
						Log.d(requestTag, response.toString());

						LayoutInflater li = ((Activity)context).getLayoutInflater();
						View showDialogView = li.inflate(R.layout.activity_register, null);
						TextView textView = showDialogView.findViewById(R.id.textView);
						//TextView textView = ((Activity)context).findViewById(R.id.textView);
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
						alertDialogBuilder.setView(showDialogView);
						alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialogInterface, int i)
							{

							}
						}).setCancelable(false).create();
						textView.setText(response.toString());
						alertDialogBuilder.show();
					}
				},
				new Response.ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error)
					{
						VolleyLog.d(requestTag, "Error: " + error.getMessage());
						Log.d(requestTag, "Error: " + error.getMessage());
					}
				});

		//	Adding Json Object request to request queue
		VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest, requestTag);
	}

	//	Volley request for JSON Array
	public void volleyJsonArrayRequest(String url, final Context context)
	{
		final String requestTag = "volleyJsonArrayRequest";

		try
		{
			JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, new JSONArray(params),
					new Response.Listener<JSONArray>()
					{
						@Override
						public void onResponse(JSONArray response)
						{
							Log.d(requestTag, response.toString());
							LayoutInflater li = ((Activity)context).getLayoutInflater();
							View showDialogView = li.inflate(R.layout.activity_register, null);
							TextView textView = showDialogView.findViewById(R.id.textView);
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
							alertDialogBuilder.setView(showDialogView);

							alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener()
							{
								@Override
								public void onClick(DialogInterface dialogInterface, int i)
								{

								}
							}).setCancelable(false).create();

							textView.setText(response.toString());
							alertDialogBuilder.show();
						}
					},
					new Response.ErrorListener()
					{
						@Override
						public void onErrorResponse(VolleyError error)
						{
							VolleyLog.d(requestTag, "Error: " + error.getMessage());
						}
					});

			//	Adding JsonArray request to request queue
			VolleySingleton.getInstance(context).addToRequestQueue(jsonArrayRequest, requestTag);
		} catch (JSONException e)
		{
			e.printStackTrace();
		}
	}

	//	Volley request for images
	public void volleyImageLoader (String url, final Context context)
	{
		final String requestTag = "volleyImageLoader";
		ImageLoader imageloader = VolleySingleton.getInstance(context).getImageLoader();

		//	We get the ImageLoader instance from the VolleySingleton
		//	and use its get() method to download the image
		//	The get() method of ImageLoader has onResponse() method to handle the response
		imageloader.get(url, new ImageLoader.ImageListener()
		{
			@Override
			public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate)
			{
				if (response.getBitmap() != null)
				{
					LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
					View showDialogView = layoutInflater.inflate(R.layout.activity_register, null);
					ImageView imageView = showDialogView.findViewById(R.id.imageView);
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
					alertDialogBuilder.setView(showDialogView);
					alertDialogBuilder.setPositiveButton("OK",
							new DialogInterface.OnClickListener()
							{
								@Override
								public void onClick(DialogInterface dialogInterface, int i)
								{

								}
							}).setCancelable(false).create();
					//	 The return type of the method is an ImageContainer object
					imageView.setImageBitmap(response.getBitmap());
					alertDialogBuilder.show();
				}
			}

			@Override
			public void onErrorResponse(VolleyError error)
			{
				Log.d(requestTag, "Image Load Error: " + error.getMessage());
				Log.e(requestTag, "Image Load Error: " + error.getMessage());
			}
		});
	}

	//	Volley cache handling
	//	Check for a cached response of an URL before making a network call
	//	If request is already present you can handle the data accordingly
	//	If request not present, then launch a network request to get data from network
	public void volleyCacheRequest(String url, Context context)
	{
		com.android.volley.Cache cache = VolleySingleton.getInstance(context).getRequestQueue().getCache();
		com.android.volley.Cache.Entry entry = cache.get(url);

		if (entry != null)
		{
			try
			{
				String data = new String(entry.data, "UTF-8");
				//	Handle the data here, like converting it to xml, json, bitmap etc.
			}
			catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			//	Cached response doesn't exists, Make a network call here.
		}

	}

	//	Invalidate cache means we are invalidating the cached data instead of deleting it.
	//	Volley will still uses the cached object until the new data are received from server.
	//	Once it receives the response from the server, it will override the older cached response.
	public void volleyInvalidateCache(String url, Context context)
	{
		VolleySingleton.getInstance(context).getRequestQueue().getCache().invalidate(url, true);
	}

	//	Disable cache for a particular URL, you can use setShouldCache()
	//	StringRequest stringRequest = new StringRequest(...);
	//	Then : stringRequest.setShouldCache(false);

	//	Deleting cache for a particular URL
	//	Use remove(url)
	public void volleyDeleteCache(String url, Context context)
	{
		VolleySingleton.getInstance(context).getRequestQueue().getCache().remove(url);
	}

	//	Deleting all the cache
	public void volleyClearCache(Context context)
	{
		VolleySingleton.getInstance(context).getRequestQueue().getCache().clear();
	}

	//	Cancel all requests
	//	If you don't pass any tag to cancelAll() method, it will cancel the request in request queue
	public void volleyCancelRequest(Context context)
	{
		VolleySingleton.getInstance(context).getRequestQueue().cancelAll(null);
	}
}
