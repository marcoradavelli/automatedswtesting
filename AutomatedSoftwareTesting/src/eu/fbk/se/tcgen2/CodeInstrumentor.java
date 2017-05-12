package eu.fbk.se.tcgen2;

import java.util.List;

import spoon.Launcher;
import spoon.SpoonAPI;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtCodeSnippetStatement;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.visitor.filter.TypeFilter;

public class CodeInstrumentor extends AbstractProcessor<CtClass> {
	public static void main(String[] args) {
		execute();
	}

	public static void execute() {
		CodeInstrumentor instrumentor = new CodeInstrumentor();
		instrumentor.instrument(43);
	}

	public void instrument(int targetLine) {
		final SpoonAPI spoon = new Launcher();
		spoon.addInputResource("src/eu/fbk/se/tcgen2/BinarySearch.java");
		spoon.buildModel();
		
		List<CtStatement> statements = spoon.getModel().getElements(new TypeFilter(CtStatement.class));
		System.out.println(statements.size());
		CtStatement targetStmt = null;        
		for (CtStatement b : statements) if (b.getPosition().getLine()==targetLine) { targetStmt=b; break; }
		System.out.println(targetStmt+"\n"+targetStmt.getPosition().getLine());
		CtBlock b = targetStmt.getParent(CtBlock.class);
		System.out.println(b+"\n\n"+b.getPosition().getLine());
		
		
		int count=0;
		//spoon.addProcessor(this); // serve?
		do {
			CtCodeSnippetStatement snippet = b.getFactory().Core().createCodeSnippetStatement();
			snippet.setValue("fitness = Math.min(fitness, "+count+")");
			b.insertBegin(snippet);
			count++;
			System.out.println(count);
			b=b.getParent(CtBlock.class);
		} while (b!=null && b.isParentInitialized());
		
		spoon.addProcessor(this);
		spoon.process();
		spoon.prettyprint();
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
