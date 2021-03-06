package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.hardware.Arm;
import org.firstinspires.ftc.teamcode.hardware.BlockGripper;
import org.firstinspires.ftc.teamcode.hardware.FoundationGripper;
import org.firstinspires.ftc.teamcode.hardware.Intake;
import org.firstinspires.ftc.teamcode.hardware.Robot;
import org.firstinspires.ftc.teamcode.misc.ButtonPress;

@TeleOp(name="Teleop Full")
public class Teleop extends LinearOpMode {

    @Override
    public void runOpMode(){
        Robot robot = new Robot(this);
        robot.init();//initalize hardware

        while(opModeIsActive()){
            //parses inputs so we don't accidentally read in two clicks when it should be one
            ButtonPress.giveMeInputs(gamepad1.a, gamepad1.b, gamepad1.x, gamepad1.y, gamepad1.dpad_up, gamepad1.dpad_down, gamepad1.dpad_right, gamepad1.dpad_left, gamepad1.right_bumper, gamepad1.left_bumper, gamepad1.left_stick_button, gamepad1.right_stick_button, gamepad2.a, gamepad2.b, gamepad2.x, gamepad2.y, gamepad2.dpad_up, gamepad2.dpad_down, gamepad2.dpad_right, gamepad2.dpad_left, gamepad2.right_bumper, gamepad2.left_bumper, gamepad2.left_stick_button, gamepad2.right_stick_button);

            //arm code
            if(gamepad2.a){
                robot.arm.fineAdjust(-1);
            }
            if(gamepad2.b){
                robot.arm.fineAdjust(1);
            }
            if (ButtonPress.isGamepad2_dpad_down_pressed()) {
                robot.arm.moveArm(Arm.Position.DOWN);
            }
            if (ButtonPress.isGamepad2_dpad_left_pressed()) {
                robot.arm.moveArm(Arm.Position.TRAVELING);
            }
            if (ButtonPress.isGamepad2_dpad_up_pressed()) {
                robot.arm.moveArm(Arm.Position.UP);
            }

            //intake code
            if(gamepad2.left_trigger > 0.5 && gamepad2.right_trigger < 0.5){ //intake
                robot.arm.moveArm(Arm.Position.INTAKING);
                robot.intake.runIntake(Intake.Direction.IN);
            }
            else if(gamepad2.left_trigger < 0.5 && gamepad2.right_trigger > 0.5){ //outtake
                robot.intake.delayOuttake(1000);
                robot.blockGripper.moveBlockGripper(BlockGripper.Position.OPEN);
                robot.arm.moveArm(Arm.Position.INTAKING);
            }
            else { //neither
                robot.intake.runIntake(Intake.Direction.STOP);
            }

            //foundation gripper code
            if (ButtonPress.isGamepad1_dpad_up_pressed()){
                if(robot.foundationGripper.position == FoundationGripper.Position.UP){
                    robot.foundationGripper.moveFoundationGripper(FoundationGripper.Position.READY);
                }
                else if (robot.foundationGripper.position == FoundationGripper.Position.READY){
                    robot.foundationGripper.moveFoundationGripper(FoundationGripper.Position.DOWN);
                }
                else if (robot.foundationGripper.position == FoundationGripper.Position.DOWN){
                    robot.foundationGripper.moveFoundationGripper(FoundationGripper.Position.UP);
                }
            }

            //block gripper code
            if (ButtonPress.isGamepad1_right_bumper_pressed()) {
                if (robot.blockGripper.position == BlockGripper.Position.OPEN) {
                    robot.blockGripper.moveBlockGripper(BlockGripper.Position.CLOSED);
                }
                else if (robot.blockGripper.position == BlockGripper.Position.CLOSED) {
                    robot.blockGripper.moveBlockGripper(BlockGripper.Position.OPEN);
                    if(robot.arm.location == Arm.Position.DOWN || robot.arm.location == Arm.Position.DEPOSIT) {
                        robot.arm.moveArm(Arm.Position.TRAVELING);
                    }
                }
            }


            //drivetrain code
            robot.drivetrain.setPower(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

            robot.drivetrain.process();
            robot.arm.process();
            robot.intake.process();
            robot.foundationGripper.process();
            robot.blockGripper.process();
        }
    }
}
