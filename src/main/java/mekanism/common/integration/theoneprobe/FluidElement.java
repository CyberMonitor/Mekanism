package mekanism.common.integration.theoneprobe;

import io.netty.buffer.ByteBuf;
import javax.annotation.Nonnull;
import mekanism.client.render.MekanismRenderer;
import mekanism.client.render.MekanismRenderer.FluidType;
import mekanism.common.MekanismLang;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidStack;

public class FluidElement extends TOPElement {

    public static int ID;

    @Nonnull
    protected final FluidStack stored;
    protected final int capacity;

    public FluidElement(@Nonnull FluidStack stored, int capacity) {
        super(0xFF000000, 0xFFFFFF);
        this.stored = stored;
        this.capacity = capacity;
    }

    public FluidElement(ByteBuf buf) {
        this(new PacketBuffer(buf).readFluidStack(), buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        new PacketBuffer(buf).writeFluidStack(stored);
        buf.writeInt(capacity);
    }

    @Override
    public int getScaledLevel(int level) {
        if (capacity == 0 || stored.getAmount() == Integer.MAX_VALUE) {
            return level;
        }
        return stored.getAmount() * level / capacity;
    }

    @Override
    public TextureAtlasSprite getIcon() {
        return stored.isEmpty() ? null : MekanismRenderer.getFluidTexture(stored, FluidType.STILL);
    }

    @Override
    public ITextComponent getText() {
        int amount = stored.getAmount();
        if (amount == Integer.MAX_VALUE) {
            return MekanismLang.GENERIC_STORED.translate(stored, MekanismLang.INFINITE);
        }
        return MekanismLang.GENERIC_STORED_MB.translate(stored, amount);
    }

    @Override
    protected boolean applyRenderColor() {
        MekanismRenderer.color(stored);
        return true;
    }

    @Override
    public int getID() {
        return ID;
    }
}