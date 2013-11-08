package de.warker.rmicluster;

import java.util.Vector;

public class TestServerTask implements IServerTask
{
	private long end = 10000000;
	
	private static long current = 0;
	private static Vector<Vector<Long>> _Result = new Vector<Vector<Long>>();
	
	@Override
	public Vector<Long> getWork(Vector result)
	{
		Vector<Long> l = new Vector<Long>();
		
		if(result != null && result.size() > 0)
			_Result.add(result);
		
		long step = 250000;
		
		l.add(current);
		l.add(current+step);
				
		current += step;
		
		if(current < end)
			return l;
		else
			return null;
	}


	@Override
	public Vector<Vector<Long>> getResults()
	{
		return _Result;
	}


}
