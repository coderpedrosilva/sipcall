package br.com.sipcall.model;

public class CallKey {
	private Object key1;
	private Object key2;
	
	public CallKey(Object key1, Object key2) {
		this.key1 = key1;
		this.key2 = key2;
	}

	@Override
	public boolean equals(Object object) {

		if (!(object instanceof CallKey))
			return false;
		CallKey ref = (CallKey) object;
		return this.key1.equals(ref.key1) && this.key2.equals(ref.key2);
	}

	@Override
	public int hashCode() {
		int hashCode = this.key1 == null ? 0 : this.key1.hashCode();
		hashCode = hashCode + (this.key2 == null ? 0 : this.key2.hashCode());
		return hashCode;
	}

	public Object getKey1() {
		return key1;
	}

	public void setKey1(Object key1) {
		this.key1 = key1;
	}

	public Object getKey2() {
		return key2;
	}

	public void setKey2(Object key2) {
		this.key2 = key2;
	}

	@Override
	public String toString() {
		return "MapKey [key1=" + key1 + ", key2=" + key2 + "]";
	}

	
	
}