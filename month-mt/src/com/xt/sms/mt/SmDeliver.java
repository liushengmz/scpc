package com.xt.sms.mt;

public class SmDeliver {
	public ByteCode bc;

	public SmDeliver() {
		this.bc = new ByteCode(1);
	}

	public void setMobileCode(String mobileCode) throws Exception {
		this.bc.AddByte((byte) 1);
		this.bc.AddShort((short) (3 + mobileCode.getBytes().length));
		this.bc.AddBytes(mobileCode.getBytes());
	}

	public void setSpCode(String spCode) throws Exception {
		this.bc.AddByte((byte) 4);
		this.bc.AddShort((short) (3 + spCode.getBytes().length));
		this.bc.AddBytes(spCode.getBytes());
	}

	public void setGameCode(String gameCode) throws Exception {
		this.bc.AddByte((byte) 2);
		this.bc.AddShort((short) (3 + gameCode.getBytes().length));
		this.bc.AddBytes(gameCode.getBytes());
	}

	public void setActionCode(String gameAction) throws Exception {
		this.bc.AddByte((byte) 3);
		this.bc.AddShort((short) (3 + gameAction.getBytes().length));
		this.bc.AddBytes(gameAction.getBytes());
	}

	public void setIsmgCode(String ismgCode) throws Exception {
		this.bc.AddByte((byte) 5);
		this.bc.AddShort((short) (3 + ismgCode.getBytes().length));
		this.bc.AddBytes(ismgCode.getBytes());
	}

	public byte[] getBytes() {
		return this.bc.getBytes();
	}
}