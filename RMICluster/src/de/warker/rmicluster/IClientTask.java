package de.warker.rmicluster;

import java.io.Serializable;

public interface IClientTask extends Serializable
{
	public <T> T execute(Object...params);
}
