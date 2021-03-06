/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This file contains an example of an iterative (Non-Linear) "OpMode".
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.

 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all iterative OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="OpMode", group="Iterative Opmode" )


public class OpMode extends com.qualcomm.robotcore.eventloop.opmode.OpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private ElapsedTime timegrab=new ElapsedTime();
    private ElapsedTime timeup=new ElapsedTime();

    public DcMotor extendDrive = null;

    Controls control = new Controls();

    private ColorSensor color_sensor;

    double relicv_grab_poz=0.2;
    double relicv_up_poz=0.8;

    Servo relicv_up;
    Servo relicv_grab;

    Servo ball_arm;
    Servo ball_color;
    /*
     * Code to run ONCE when the driver hits INIT
     */

    @Override
    public void init() {

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).


    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {

    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {

        control.leftDrive = hardwareMap.get(DcMotor.class, "left_drive");
        control.leftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        control.rightDrive = hardwareMap.get(DcMotor.class, "right_drive");
        control.rightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        control.upDrive = hardwareMap.get(DcMotor.class, "up_drive");
        extendDrive= hardwareMap.get(DcMotor.class, "extend_drive");


        control.grab_cube_left=hardwareMap.get(Servo.class,"grab_cube_left");
        control.grab_cube_left.setPosition(1);
        control.grab_cube_right=hardwareMap.get(Servo.class,"grab_cube_right");


        control.leftDrive.setDirection(DcMotor.Direction.FORWARD);
        control.rightDrive.setDirection(DcMotor.Direction.REVERSE);
        control.upDrive.setDirection(DcMotor.Direction.FORWARD);
        extendDrive.setDirection(DcMotor.Direction.FORWARD);

        ball_color = hardwareMap.get(Servo.class,"ball_servo");
        ball_arm = hardwareMap.get(Servo.class,"ball_arm");
        ball_arm.setPosition(1);
        ball_color.setPosition(0.5);

        relicv_up=hardwareMap.get(Servo.class,"relicv_up");
        relicv_grab=hardwareMap.get(Servo.class,"relicv_grab");

        control.gyro = (ModernRoboticsI2cGyro) hardwareMap.gyroSensor.get("gyro");
        runtime.reset();



    }
    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        double drive = -gamepad1.left_stick_y;
        double turn = gamepad1.right_stick_x;

        control.navigate(drive, turn);

        if (gamepad1.right_bumper)




            extendDrive.setPower(-1);
        else
        if(!gamepad1.left_bumper)
        {
           extendDrive.setPower(0);
        }


        if (gamepad1.left_bumper)
           extendDrive.setPower(1);
        else
        if(!gamepad1.right_bumper)
        {
            extendDrive.setPower(0);
        }

        if (gamepad1.x && timegrab.seconds() > 0.3)
            if (relicv_grab_poz ==0.25) {
                relicv_grab_poz = 0.9;
                relicv_grab.setPosition(0.9);
                timegrab.reset();
        } else {
                relicv_grab_poz = 0.25;
                relicv_grab.setPosition(0.25);
                timegrab.reset();
            }

        if (gamepad1.y && timeup.seconds() > 0.3)
            if (relicv_up_poz == 0.6) {
                relicv_up_poz = 0;
                relicv_up.setPosition(0);
                timeup.reset();

            } else {
                relicv_up_poz =0.6;
                timeup.reset();
                relicv_up.setPosition(0.6);
                control.sleep(20);

            }
        

        if (gamepad1.dpad_up)
        {
            control.lifter_up();
        }
        else
        {
            if(!gamepad1.dpad_down)
            {
                control.lifter_stop();
            }
        }

        if(gamepad1.dpad_down )
        {
            control.lifter_down();
        }
        else
        {
            if(!gamepad1.dpad_up)
            {
                control.lifter_stop();
            }
        }

        if(gamepad1.a)
         if(timegrab.seconds()>0.3) {
            control.grab();
         }

        if(runtime.seconds()>120 && gamepad1.b && control.powerRatio>40)
        {control.powerRatio=40;}
        else
        {if(runtime.seconds()>120 && gamepad1.b)
            control.powerRatio=75;
        }


        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Status", "a = grab");
        telemetry.addData("Status", "b = up");
        telemetry.addData("Status", "y  = extend");
        telemetry.addData("Status", "servo left"+control.grab_cube_left.getPosition());
        telemetry.addData("Status", "servo right"+control.grab_cube_right.getPosition());
        telemetry.addData("Status", "servo up"+relicv_up.getPosition());
        telemetry.addData("Status", "servo grab"+relicv_grab.getPosition());
        telemetry.addData("Status",String.format("right trig" + ( gamepad1.right_bumper)));
        telemetry.addData("Status",String.format("left trig" + ( gamepad1.left_bumper)));



    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

}
