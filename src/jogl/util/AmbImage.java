package jogl.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import jogl.GLStatic;
import util.system.fake.FakeImage;
import util.system.fake.awt.FIBI;

public class AmbImage implements FakeImage {

	private final byte[] stream;
	private final File file;
	private final AmbImage par;
	private final int[] cs;
	private boolean force, failed;

	private FIBI bimg;
	private GLImage gl;

	protected AmbImage(BufferedImage b) {
		stream = null;
		file = null;
		par = null;
		cs = null;
		bimg = (FIBI) FIBI.build(b);
		force = true;
	}

	protected AmbImage(byte[] is) {
		stream = is;
		file = null;
		par = null;
		cs = null;
	}

	protected AmbImage(File f) {
		stream = null;
		file = f;
		par = null;
		cs = null;
	}

	private AmbImage(AmbImage img, int... c) {
		stream = null;
		file = null;
		par = img;
		cs = c;
	}

	@Override
	public BufferedImage bimg() {
		checkBI();
		if (bimg == null)
			return null;
		return bimg.bimg();
	}

	@Override
	public int getHeight() {
		check();
		return bimg != null ? bimg.getHeight() : gl.getHeight();
	}

	@Override
	public int getRGB(int i, int j) {
		checkBI();
		return bimg.getRGB(i, j);
	}

	@Override
	public FakeImage getSubimage(int i, int j, int k, int l) {
		return new AmbImage(this, i, j, k, l);
	}

	@Override
	public int getWidth() {
		check();
		return bimg != null ? bimg.getWidth() : gl.getWidth();
	}

	@Override
	public Object gl() {
		checkGL();
		return gl;
	}

	@Override
	public void setRGB(int i, int j, int p) {
		forceBI();
		bimg.setRGB(i, j, p);
	}

	private void check() {
		if (gl != null || bimg != null)
			return;
		if (GLStatic.ALWAYS_GLIMG || GLGraphics.count > 0)
			checkGL();
		else
			checkBI();
	}

	private void checkBI() {
		if (bimg != null || failed)
			return;
		try {
			if (stream != null)
				bimg = (FIBI) FIBI.builder.build(stream);
			else if (file != null)
				bimg = (FIBI) FIBI.builder.build(file);
			else {
				par.checkBI();
				bimg = par.bimg.getSubimage(cs[0], cs[1], cs[2], cs[3]);
			}
			if (bimg == null)
				failed = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void checkGL() {
		if (gl != null)
			return;
		try {
			if (force)
				gl = new GLImage(bimg.bimg());
			else if (stream != null)
				gl = new GLImage(stream);
			else if (file != null)
				gl = new GLImage(file);
			else {
				par.checkGL();
				gl = par.gl.getSubimage(cs[0], cs[1], cs[2], cs[3]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void forceBI() {
		checkBI();
		force = true;
		gl = null;
	}

}