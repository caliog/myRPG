package org.caliog.npclib.v1_8_R3;

import java.net.SocketAddress;

import io.netty.channel.AbstractChannel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.EventLoop;

public class NPCChannel extends AbstractChannel {

	public NPCChannel() {
		super(null);
	}

	@Override
	public boolean isActive() {
		return false;
	}

	@Override
	public boolean isOpen() {
		return false;
	}

	@Override
	protected void doBeginRead() throws Exception {
	}

	@Override
	protected void doBind(SocketAddress arg0) throws Exception {
	}

	@Override
	protected void doClose() throws Exception {
	}

	@Override
	protected void doDisconnect() throws Exception {
	}

	@Override
	protected SocketAddress localAddress0() {
		return null;
	}

	@Override
	protected AbstractUnsafe newUnsafe() {
		return null;
	}

	@Override
	protected SocketAddress remoteAddress0() {
		return null;
	}

	@Override
	protected void doWrite(ChannelOutboundBuffer arg0) throws Exception {

	}

	@Override
	protected boolean isCompatible(EventLoop arg0) {
		return false;
	}

	@Override
	public ChannelConfig config() {
		return null;
	}

	@Override
	public ChannelMetadata metadata() {
		return null;
	}
}