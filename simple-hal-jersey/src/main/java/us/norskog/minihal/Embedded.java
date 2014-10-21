package us.norskog.minihal;

public @interface Embedded {
	String name();
	String path();
	LinkSet links();
}
