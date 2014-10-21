package us.norskog.simplehal;

public @interface Embedded {
	String name();
	String path();
	LinkSet links();
}
