package eu.fbk.se.tcgen2;

import spoon.Launcher;
import spoon.SpoonAPI;
import spoon.processing.AbstractProcessor;
import spoon.processing.Processor;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.ModifierKind;

public class TrySpoong extends AbstractProcessor<CtClass>{
	public static void main(String[] args) {
		final SpoonAPI spoon = new Launcher();
		spoon.addInputResource("src/eu/fbk/se/tcgen2/BinarySearch.java");
		spoon.buildModel();
		Processor processor = new TrySpoong();
		spoon.addProcessor(processor);
		spoon.process();
		spoon.prettyprint();
		
		//assertThat(type.getField("i")).withProcessor(new MyProcessor()).isEqualTo("public int j;");
	}

	@Override
	public void process(CtClass clazz) {
		CtField snippet = getFactory().Core().createField();
		snippet.setSimpleName("fitness");
		snippet.setType(getFactory().Code().createCtTypeReference(int.class));
		snippet.addModifier(ModifierKind.PUBLIC);
		snippet.addModifier(ModifierKind.STATIC);
		clazz.addField(snippet);
	}
}
