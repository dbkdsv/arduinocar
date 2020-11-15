#define inertia 50
#define echoPin 10
#define trigPin 9
#define MAX_DISTANCE 200; 

#include <AFMotor.h>
AF_DCMotor motor1(1);
AF_DCMotor motor2(2);
AF_DCMotor motor3(3);
AF_DCMotor motor4(4);

#include <NewPing.h>// documentation: https://bitbucket.org/teckel12/arduino-new-ping/wiki/Home
NewPing sonar(trigPin, echoPin, MAX_DISTANCE);

int myspeed = 0;

void setup() {
  Serial.begin(9600);
  //  motor1.setSpeed(speed);
  //  motor2.setSpeed(speed);
  //  motor3.setSpeed(speed);
  //  motor4.setSpeed(speed);
}

void loop() {
  Serial.println(sonar.ping_cm());
  unsigned char val = Serial.read();
  switch (val) {
    case 'W': {
        if (myspeed <= 255 - inertia)
          myspeed += inertia;
        else myspeed = 255;
        motor1.setSpeed(abs(myspeed));
        motor2.setSpeed(abs(myspeed));
        motor3.setSpeed(abs(myspeed));
        motor4.setSpeed(abs(myspeed));
        if (myspeed > 0) {
          motor1.run(FORWARD);
          motor2.run(FORWARD);
          motor3.run(FORWARD);
          motor4.run(FORWARD);
        }
        else {
          motor1.run(BACKWARD);
          motor2.run(BACKWARD);
          motor3.run(BACKWARD);
          motor4.run(BACKWARD);
        }
        break;
      }
    case 'S': {
        if (myspeed >= -255 + inertia)
          myspeed -= inertia;
        else
          myspeed = -255;
        motor1.setSpeed(abs(myspeed));
        motor2.setSpeed(abs(myspeed));
        motor3.setSpeed(abs(myspeed));
        motor4.setSpeed(abs(myspeed));
        if (myspeed > 0){
          motor1.run(FORWARD);
          motor2.run(FORWARD);
          motor3.run(FORWARD);
          motor4.run(FORWARD);
        }
        else{
          motor1.run(BACKWARD);
          motor2.run(BACKWARD);
          motor3.run(BACKWARD);
          motor4.run(BACKWARD);
        }
        break;
      }
    case 'A': {
        motor1.setSpeed(255);
        motor2.setSpeed(255);
        motor3.setSpeed(255);
        motor4.setSpeed(255);
        motor1.run(BACKWARD);
        motor2.run(FORWARD);
        motor3.run(FORWARD);
        motor4.run(BACKWARD);
        break;
      }
    case 'D': {
        motor1.setSpeed(255);
        motor2.setSpeed(255);
        motor3.setSpeed(255);
        motor4.setSpeed(255);
        motor1.run(FORWARD);
        motor2.run(BACKWARD);
        motor3.run(BACKWARD);
        motor4.run(FORWARD);
        break;
      }
    default: {
        if (abs(myspeed) >= inertia)
          myspeed -= myspeed / abs(myspeed) * inertia / 2;
        else
          myspeed = 0;
        motor1.setSpeed(abs(myspeed));
        motor2.setSpeed(abs(myspeed));
        motor3.setSpeed(abs(myspeed));
        motor4.setSpeed(abs(myspeed));
        if (myspeed > 0) {
          motor1.run(FORWARD);
          motor2.run(FORWARD);
          motor3.run(FORWARD);
          motor4.run(FORWARD);
        }
        else {
          motor1.run(BACKWARD);
          motor2.run(BACKWARD);
          motor3.run(BACKWARD);
          motor4.run(BACKWARD);
        }
        break;
      }
  }
  delay (50);
  motor1.run(RELEASE);
  motor2.run(RELEASE);
  motor3.run(RELEASE);
  motor4.run(RELEASE);
}
