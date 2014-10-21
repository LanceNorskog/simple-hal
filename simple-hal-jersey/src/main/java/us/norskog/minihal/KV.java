package us.norskog.minihal;

public class KV {

	private String key;
	private Object value;

	KV(String key, Object value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public Object getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return "[key="+key.toString() + ",value="+value.toString()+"]";
	}
}
