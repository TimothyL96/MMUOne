package club.itsociety.mmuone;

import java.lang.reflect.Constructor;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class UserData
{
	public static int studentID;
	public static String macAddr;

	UserData()
	{
		//	Get the MAC address
		macAddr = getMacAddr();
	}

	//	TODO SQLite

	//	Get MAC address of Android phone
	public String getMacAddr()
	{
		try
		{
			List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());

			for (NetworkInterface networkInterface : all)
			{
				if (!networkInterface.getName().equalsIgnoreCase("wlan0"))
					continue;

				byte[] macAddrBytes = networkInterface.getHardwareAddress();

				if (macAddrBytes == null)
				{
					return "";
				}

				StringBuilder res = new StringBuilder();

				for (byte b : macAddrBytes)
				{
					res.append(String.format("%02X:", b));
				}

				if (res.length() > 0)
				{
					res.deleteCharAt(res.length() - 1);
				}

				//	Return the MAC address
				return res.toString();
			}
		}
		catch (Exception e)
		{
			//	Exception
		}

		return "";
	}
}
