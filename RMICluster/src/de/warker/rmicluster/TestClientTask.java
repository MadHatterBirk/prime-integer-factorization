package de.warker.rmicluster;

import java.util.Vector;

public class TestClientTask implements IClientTask
{

	@Override
	public Vector<Long> execute(Object... params)
	{
		Long start = (Long)params[0];
		Long end = (Long)params[1];
		
		System.out.println("Client got task: start: "+ start + " end: " + end);
		
		Vector<Long> r = new Vector<Long>();
		boolean found = false;
		
		for(; start < end; start++)
		{
			found = false;
			
			for(Long l = 2L; l < start/2; l++)
			{
				if(start%l == 0)
				{
					found = true;
					break;
				}
			}
			
			if(!found)
				r.add(start);
		}
		
		return r;
	}

}
