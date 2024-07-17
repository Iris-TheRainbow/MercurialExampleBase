package org.firstinspires.ftc.teamcode.subsystems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import dev.frozenmilk.mercurial.subsystems.Subsystem;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.commands.LambdaCommand;
import dev.frozenmilk.mercurial.subsystems.SDKSubsystem;

import kotlin.annotation.MustBeDocumented;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@SuppressWarnings("unused")
public class mecanumbase extends SDKSubsystem {

    private DcMotorEx frontLeft, rearLeft, frontRight, rearRight;
    private static final mecanumbase INSTANCE = new mecanumbase();

    public static mecanumbase getInstance() {
        return INSTANCE;
    }

    private mecanumbase() {
        frontLeft = getHardwareMap().get(DcMotorEx.class, "frontLeft");
        frontRight = getHardwareMap().get(DcMotorEx.class, "frontRight");
        rearLeft = getHardwareMap().get(DcMotorEx.class, "rearLeft");
        rearRight = getHardwareMap().get(DcMotorEx.class, "rearRight");

        setDefaultCommand(runDrive());
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @MustBeDocumented
    @Inherited
    public @interface Attach {
    }

    private final Dependency<?> dependency = Subsystem.DEFAULT_DEPENDENCY.and(
        new SingleAnnotation<>(Attach.class)
    );

    @NonNull
    @Override
    public Dependency<?> getDependency() {
        return dependency;
    }

    private void mecanumRun(double X, double Y, double turn, double heading){
        double rotatedX = (X * Math.cos(-heading) - Y * Math.sin(-heading)) * 1.1 ;
        double rotatedY = X * Math.sin(-heading) + Y * Math.cos(-heading);
        double rfPower = (rotatedY + rotatedX + turn);
        double rrPower = (rotatedY - rotatedX + turn);
        double lfPower = (rotatedY - rotatedX - turn);
        double lrPower = (rotatedY + rotatedX - turn);
        double max = Math.max(Math.max(rfPower, rrPower), Math.max(lfPower, lrPower));
        if ( max > 1) {
            rfPower /= max;
            rrPower /= max;
            lfPower /= max;
            lrPower /= max;
        }
        frontRight.setPower(rfPower);
        frontLeft.setPower(lfPower);
        rearRight.setPower(rrPower);
        rearLeft.setPower(lrPower);
    }
    private void mecanumRun(double forward, double strafe, double turn){
        mecanumRun(forward, strafe, turn, 0.0);
    }



    @NonNull
    public static LambdaCommand runDrive() {
        return new LambdaCommand()
            .addRequirements(INSTANCE)
            .setInit(() -> {INSTANCE.mecanumRun(Mercurial.gamepad1().leftStickX(), -Mercurial.gamepad1().leftStickY(), Mercurial.gamepad1().leftStickX());
            });
    }
}
