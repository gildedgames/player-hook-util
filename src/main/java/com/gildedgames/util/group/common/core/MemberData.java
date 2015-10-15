package com.gildedgames.util.group.common.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.gildedgames.util.core.UtilCore;
import com.gildedgames.util.group.GroupCore;
import com.gildedgames.util.group.common.IGroupHook;
import com.gildedgames.util.group.common.player.GroupMember;
import com.gildedgames.util.io_manager.factory.IOBridge;
import com.gildedgames.util.io_manager.io.IO;
import com.gildedgames.util.io_manager.util.IOUtil;

import net.minecraft.entity.player.EntityPlayer;

public class MemberData implements IO<IOBridge, IOBridge>, Iterable<GroupMember>
{
	private final List<GroupMember> members = new ArrayList<GroupMember>();

	private final List<GroupMember> invitedMembers = new ArrayList<GroupMember>();

	private List<IGroupHook> hooks = new ArrayList<IGroupHook>();

	protected void setHooks(List<IGroupHook> hooks)
	{
		this.hooks = hooks;
	}

	@Override
	public void write(IOBridge output)
	{
		int memberSize = this.members.size();
		output.setInteger("memberSize", memberSize);
		for (int i = 0; i < memberSize; i++)
		{
			IOUtil.setUUID(this.members.get(i).getProfile().getUUID(), output, "member" + i);
		}

		int invitedSize = this.invitedMembers.size();
		output.setInteger("memberSize", invitedSize);
		for (int i = 0; i < invitedSize; i++)
		{
			IOUtil.setUUID(this.invitedMembers.get(i).getProfile().getUUID(), output, "invited" + i);
		}

		IOUtil.setIOList("hooks", this.hooks, output);
	}

	@Override
	public void read(IOBridge input)
	{
		this.members.clear();
		this.invitedMembers.clear();

		int memberSize = input.getInteger("memberSize");
		for (int i = 0; i < memberSize; i++)
		{
			this.members.add(GroupMember.get(IOUtil.getUUID(input, "member" + i)));
		}

		int invitedSize = input.getInteger("memberSize");
		for (int i = 0; i < invitedSize; i++)
		{
			this.members.add(GroupMember.get(IOUtil.getUUID(input, "invited" + i)));
		}

		this.hooks = IOUtil.getIOList("hooks", input);
	}

	protected void join(GroupMember member)
	{
		this.invitedMembers.remove(member);

		if (this.members.contains(member))
		{
			UtilCore.print("Tried to join group but player " + member.getProfile().getUsername() + " was already in it");
			return;
		}

		this.members.add(member);

		for (IGroupHook hook : this.hooks)
		{
			hook.onMemberAdded(member);
		}
	}

	protected void leave(GroupMember member)
	{
		if (this.assertMember(member))
		{
			this.members.remove(member);
			for (IGroupHook hook : this.hooks)
			{
				hook.onMemberRemoved(member);
			}
		}
	}

	protected void invite(GroupMember member)
	{
		if (this.members.contains(member))
		{
			UtilCore.print("Tried to invite player who is already a member: " + member.getProfile().getUsername());
			return;
		}

		for (IGroupHook hook : this.hooks)
		{
			hook.onMemberInvited(member);
		}

		this.invitedMembers.add(member);
	}

	protected void removeInvitation(GroupMember member)
	{
		if (this.members.contains(member))
		{
			UtilCore.print("Tried to remove invitation of a player who is already a member: " + member.getProfile().getUsername());
			return;
		}

		if (!this.invitedMembers.contains(member))
		{
			UtilCore.print("Tried to remove invitation of a player who wasn't invited: " + member.getProfile().getUsername());
			return;
		}

		for (IGroupHook hook : this.hooks)
		{
			hook.onInviteRemoved(member);
		}

		this.invitedMembers.remove(member);
	}

	protected boolean assertMember(GroupMember member)
	{
		if (!this.members.contains(member))
		{
			UtilCore.print("Trying to do something with a player who is not a member");
			return false;
		}
		return true;
	}

	protected void talkTo()
	{
		//TODO
	}

	@Override
	public Iterator<GroupMember> iterator()
	{
		return this.members.iterator();
	}

	public boolean contains(GroupMember groupMember)
	{
		return this.members.contains(groupMember);
	}

	public int size()
	{
		return this.members.size();
	}

	public List<GroupMember> getMembers()
	{
		return new ArrayList<GroupMember>(this.members);
	}

	public boolean isInvited(EntityPlayer player)
	{
		return this.isInvited(GroupCore.locate().getPlayers().get(player));
	}

	public boolean isInvited(GroupMember player)
	{
		return !this.members.contains(player) && this.invitedMembers.contains(player);
	}
}