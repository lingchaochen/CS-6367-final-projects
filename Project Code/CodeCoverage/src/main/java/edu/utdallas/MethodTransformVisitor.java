package edu.utdallas;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;


class MethodTransformVisitor extends MethodVisitor implements Opcodes {
	String mName, className;
	int line = 0;
	//int line, executedline;

	public MethodTransformVisitor(final MethodVisitor mv, String className, String name) {
		super(ASM5, mv);
		this.mName = name;
		this.className = className;
	}

	@Override
	public void visitLineNumber(int line, Label start) {
		this.line = line;
		//---------
		mv.visitLdcInsn(className.replace('/', '.') + ":" + line);
		mv.visitMethodInsn(INVOKESTATIC, "edu/utdallas/util/Helper", "addExecutedLine", "(Ljava/lang/String;)V", false);
		//---------
		super.visitLineNumber(line, start);
	}
	
	@Override
	public void visitLabel(Label label) {
		mv.visitLdcInsn(className.replace('/', '.') + ":" + this.line);
		mv.visitMethodInsn(INVOKESTATIC, "edu/utdallas/util/Helper", "addExecutedLine", "(Ljava/lang/String;)V", false);
		super.visitLabel(label);
	}
}
