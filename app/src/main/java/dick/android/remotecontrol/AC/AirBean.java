package dick.android.remotecontrol.AC;

public class AirBean {
	private int mPower;
	private int mMode;
	private int mTmp;
	private int menergy;
	private int mWindCount;
	private int mWindDir;

	public AirBean(int mPower, int mTmp, int mMode, int menergy, int mWindDir, int mWindCount) {
		this.mPower = mPower;
		this.mMode = mMode;
		this.mTmp = mTmp;
		this.menergy = menergy;
		this.mWindCount = mWindCount;
		this.mWindDir = mWindDir;
	}

    public int getmPower() {
        return mPower;
    }

    public void setmPower(int mPower) {
        this.mPower = mPower;
    }

    public int getmMode() {
        return mMode;
    }

    public void setmMode(int mMode) {
        this.mMode = mMode;
    }

    public int getmTmp() {
        return mTmp;
    }

    public void setmTmp(int mTmp) {
        this.mTmp = mTmp;
    }

    public int getMenergy() {
        return menergy;
    }

    public void setMenergy(int menergy) {
        this.menergy = menergy;
    }

    public int getmWindCount() {
        return mWindCount;
    }

    public void setmWindCount(int mWindCount) {
        this.mWindCount = mWindCount;
    }

    public int getmWindDir() {
        return mWindDir;
    }

    public void setmWindDir(int mWindDir) {
        this.mWindDir = mWindDir;
    }
}
