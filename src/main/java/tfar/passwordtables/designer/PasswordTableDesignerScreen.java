package tfar.passwordtables.designer;

import crafttweaker.mc1120.CraftTweaker;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import tfar.passwordtables.*;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class PasswordTableDesignerScreen extends GuiContainer {
	private final ResourceLocation background;

	private GuiTextField nameField;
	private int textX;
	private int textY;

	public PasswordTableDesignerScreen(PasswordTableDesignerMenu menu) {
		super(menu);
		background = menu.size == 3 ? CRAFTING_TABLE_GUI_TEXTURES : new ResourceLocation(PasswordTables.MODID,"textures/gui/"+menu.size+"x"+menu.size+".png");
		this.xSize += (menu.size - 3) * 18;
		this.ySize += (menu.size - 3) * 18 + 3;
	}

	private static final ResourceLocation CRAFTING_TABLE_GUI_TEXTURES = new ResourceLocation("textures/gui/container/crafting_table.png");

	public ToggleButton toggleButton;

	public void initGui() {
		super.initGui();
		Keyboard.enableRepeatEvents(true);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;

		int size = ((PasswordTableDesignerMenu)inventorySlots).size;

		textX = 44 + size * 18;
		textY = 32 + size * 9 + (size > 8 ? 18 : 0);

		this.nameField = new GuiTextField(0, this.fontRenderer, i + textX + 1, j + textY + 1, 64, 12);
		this.nameField.setTextColor(-1);
		this.nameField.setDisabledTextColour(-1);
		this.nameField.setEnableBackgroundDrawing(false);
		this.nameField.setMaxStringLength(35);

		GuiButton save = new GuiButton(1996,i + textX + 15,j + textY + 20,38,20,"save");

		addButton(save);

		this.toggleButton = new ToggleButton(1997,i + textX ,j + textY - 52,64,20,"shaped","shapeless",true);

		addButton(toggleButton);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 1996) {
			saveRecipe();
		} else if (button.id == 1997) {
			((ToggleButton)button).toggle = !((ToggleButton)button).toggle;
		} else
		super.actionPerformed(button);
	}

	public void saveRecipe() {
		String recipe = CTHelper.toCTScript((PasswordTableDesignerBlockEntity)((PasswordTableDesignerMenu)this.inventorySlots).world.getTileEntity(((PasswordTableDesignerMenu)this.inventorySlots).pos),toggleButton.toggle,nameField.getText());

		if (recipe == null)return;

		File scriptFile = new File(new File("scripts"), String.format("/%s.zs", "passwordtable"));
		if(!scriptFile.exists()) {
			generateFile(scriptFile);
		}
		try {
			List<String> lines = new LinkedList<>();
			BufferedReader reader = new BufferedReader(new FileReader(scriptFile));
			String line;
			while((line = reader.readLine()) != null) {
				lines.add(line);
			}
			if(lines.isEmpty()) {
				generateFile(scriptFile);
				while((line = reader.readLine()) != null) {
					lines.add(line);
				}
			}
			reader.close();
			PrintWriter writer = new PrintWriter(new FileWriter(scriptFile));
			for(int i = 0; i < lines.size(); i++) {
				String beforeLine = "";
				if(i > 0)
					beforeLine = lines.get(i - 1);

				String lined = lines.get(i);
				if(beforeLine.trim().equals("//#Add")) {
					writer.println(recipe);
				}
				if(!lined.isEmpty()) {
					writer.println(lined);
				}

			}
			writer.close();
		} catch(IOException e) {
			CraftTweaker.LOG.catching(e);
		}

	}

	public void generateFile(File f) {
		try {
			f.createNewFile();
			PrintWriter writer = new PrintWriter(new FileWriter(f));
			writer.println("//This file was created via The Designer Screen");
			writer.println("//Don't touch me!");
			writer.println("//#Add");
			writer.println();
			writer.println("//File End");
			writer.close();
		} catch(IOException e) {
			CraftTweaker.LOG.catching(e);
		}
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
