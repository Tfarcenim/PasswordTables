package tfar.passwordtables;

import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class PasswordTableScreen extends GuiContainer {
	private final ResourceLocation background;

	private GuiTextField nameField;
	private int textX;
	private int textY;

	public PasswordTableScreen(PasswordTableMenu menu) {
		super(menu);
		background = menu.size == 3 ? CRAFTING_TABLE_GUI_TEXTURES : new ResourceLocation(PasswordTables.MODID,"textures/gui/"+menu.size+"x"+menu.size+".png");
		this.xSize += (menu.size - 3) * 18;
		this.ySize += (menu.size - 3) * 18 + 3;
	}

	private static final ResourceLocation CRAFTING_TABLE_GUI_TEXTURES = new ResourceLocation("textures/gui/container/crafting_table.png");


	public void initGui() {
		super.initGui();
		Keyboard.enableRepeatEvents(true);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;

		int size = ((PasswordTableMenu)inventorySlots).size;

		textX = 44 + size * 18;
		textY = 32 + size * 9 + (size > 8 ? 18 : 0);

		this.nameField = new GuiTextField(0, this.fontRenderer, i + textX + 1, j + textY+1, 64, 12);
		this.nameField.setTextColor(-1);
		this.nameField.setDisabledTextColour(-1);
		this.nameField.setEnableBackgroundDrawing(false);
		this.nameField.setMaxStringLength(35);
	}

	/**
	 * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
	 */
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		this.nameField.mouseClicked(mouseX, mouseY, mouseButton);
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
		GlStateManager.disableLighting();
		GlStateManager.disableBlend();
		this.nameField.drawTextBox();
	}

	/**
	 * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
	 */
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		if (this.nameField.textboxKeyTyped(typedChar, keyCode))
		{
			this.syncPassword();
		}
		else
		{
			super.keyTyped(typedChar, keyCode);
		}
	}

	private void syncPassword() {
		String s = this.nameField.getText();
		PacketHandler.INSTANCE.sendToServer(new C2SPasswordPacket(s));
	}


	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.fontRenderer.drawString(I18n.format("container.crafting"), 28, 6, 4210752);
		this.fontRenderer.drawString(I18n.format("container.inventory"), 8, this.ySize - 96, 4210752);
	}

	/**
	 * Draws the background layer of this container (behind the items).
	 */
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(background);
		int i = this.guiLeft;
		int j = (this.height - this.ySize) / 2;
		if (xSize > 250) {
			drawModalRectWithCustomSizedTexture(i,j,0,0,this.xSize,this.ySize,512,512);
		} else {
			this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
		}

		this.drawGradientRect(guiLeft + textX, guiTop + textY,
						guiLeft + textX + 65,
						guiTop + textY + 11, 0xFF000000,0xFF000000);
	}
}
