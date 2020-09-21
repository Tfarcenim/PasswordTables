package tfar.passwordtables;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

// not threadsafe!
public class C2SPasswordPacket implements IMessage {

	private String password;

	public C2SPasswordPacket() {
	}

	public C2SPasswordPacket(String password) {
		this.password = password;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		password = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf,password);
	}

	public static class Handler implements IMessageHandler<C2SPasswordPacket, IMessage> {
		@Override
		public IMessage onMessage(C2SPasswordPacket message, MessageContext ctx) {
			// Always use a construct like this to actually handle your message. This ensures that
			// youre 'handle' code is run on the main Minecraft thread. 'onMessage' itself
			// is called on the networking thread so it is not safe to do a lot of things
			// here.
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}

		private void handle(C2SPasswordPacket message, MessageContext ctx) {
			// This code is run on the server side. So you can do server-side calculations here
			EntityPlayerMP playerEntity = ctx.getServerHandler().player;
			Container container = playerEntity.openContainer;
			IThreadListener mainThread = (WorldServer) ctx.getServerHandler().player.world;
			mainThread.addScheduledTask(() -> {
				if(container instanceof PasswordTableMenu) {
					((PasswordTableMenu) container).setPassword(message.password);
				}
			});
		}
	}
}