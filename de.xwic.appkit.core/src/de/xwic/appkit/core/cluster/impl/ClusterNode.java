/**
 * 
 */
package de.xwic.appkit.core.cluster.impl;

import java.util.Date;

import de.xwic.appkit.core.cluster.CommunicationException;
import de.xwic.appkit.core.cluster.INode;
import de.xwic.appkit.core.cluster.Message;
import de.xwic.appkit.core.cluster.NodeAddress;
import de.xwic.appkit.core.cluster.Response;

/**
 * Represents a node within the cluster.
 * 
 * @author lippisch
 */
public class ClusterNode implements INode {

	private static int _INT_NUM = 0;
	
	private NodeAddress nodeAddress;
	private OutboundChannel channel;

	private NodeStatus status = NodeStatus.NEW; 
	private String name = null;
	private int internalNumber;
	private int masterPriority;
	
	private Date disconnectedSince = null;
	
	/**
	 * @param newNode
	 * @param oc
	 */
	public ClusterNode(NodeAddress nodeAddress) {
		this.nodeAddress = nodeAddress;
		internalNumber = _INT_NUM++;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.cluster.INode#getAddress()
	 */
	@Override
	public NodeAddress getAddress() {
		return nodeAddress;
	}

	/**
	 * Send a message to the node.
	 * @param message
	 * @return
	 * @throws CommunicationException
	 */
	public Response sendMessage(Message message) throws CommunicationException {
		return channel.sendMessage(message);
	}

	/**
	 * @return the status
	 */
	public NodeStatus getStatus() {
		return status;
	}

	/**
	 * A connection was established
	 * @param outboundChannel
	 */
	public void _connected(OutboundChannel outboundChannel) {
		this.channel = outboundChannel;
		status = NodeStatus.CONNECTED;
		disconnectedSince = null;
	}
	
	/**
	 * A connection was disconnected.
	 */
	public void _disconnected() {
		this.channel = null;
		status = NodeStatus.DISCONNECTED;
		disconnectedSince = new Date();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the internalNumber
	 */
	public int getInternalNumber() {
		return internalNumber;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (name == null) {
			sb.append("Unnamed");
		} else {
			sb.append(name);
		}
		sb.append(" [").append(nodeAddress).append("]")
		.append(" #").append(internalNumber);
		return sb.toString();
	}

	/**
	 * @return the disconnectedSince
	 */
	public Date getDisconnectedSince() {
		return disconnectedSince;
	}

	/**
	 * Node gets disabled, no more retries. 
	 */
	public void _disable() {
		status = NodeStatus.DISABLED;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.cluster.INode#sameNode(de.xwic.appkit.cluster.INode)
	 */
	@Override
	public boolean sameNode(INode n) {
		if (n == null) {
			return false;
		}
		ClusterNode cn = (ClusterNode)n;
		if (nodeAddress == null || cn.nodeAddress == null) {
			// return FALSE! Both nodes can not be identified..
			return false;
		}
		return nodeAddress.equals(cn.nodeAddress);
		
	}

	/**
	 * @return the masterPriority
	 */
	public int getMasterPriority() {
		return masterPriority;
	}

	/**
	 * @param masterPriority the masterPriority to set
	 */
	public void setMasterPriority(int masterPriority) {
		this.masterPriority = masterPriority;
	}
	
}
