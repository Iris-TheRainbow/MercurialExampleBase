package org.firstinspires.ftc.teamcode.subsystems;

import androidx.annotation.NonNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.mercurial.commands.LambdaCommand;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import kotlin.annotation.MustBeDocumented;


public class mecanumbase implements Subsystem {
    private static final mecanumbase INSTANCE = new mecanumbase();
    public static mecanumbase getInstance() {
        return INSTANCE;
    }

    private mecanumbase() {
        setDefaultCommand(simpleCommand());
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @MustBeDocumented
    @Inherited
    public @interface Attach{}
    private final Dependency<?> dependency =
            Subsystem.DEFAULT_DEPENDENCY
                    .and(new SingleAnnotation<>(Attach.class));

    @NonNull
    @Override
    public Dependency<?> getDependency() {
        return dependency;
    }
    @NonNull
    public static LambdaCommand simpleCommand() {
        return new LambdaCommand()
                .addRequirements(INSTANCE)
                .setInit(() -> )
                .setEnd(interrupted -> {
                    if (!interrupted) getMotor().setPower(0.0);
                });
    }

}