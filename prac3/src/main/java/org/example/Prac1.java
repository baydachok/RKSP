package org.example;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import java.util.Random;

public class Prac1 {

  enum SensorType {
    TEMPERATURE_SENSOR, CO2_SENSOR
  }

  record SensorMessage(Integer value, SensorType sensorType) {}

  static class TemperatureSensor extends Observable<SensorMessage> {
    private final PublishSubject<SensorMessage> subject = PublishSubject.create();
    private final SensorType sensorType = SensorType.TEMPERATURE_SENSOR;

    public void start() {
      new Thread(() -> {
        while (true) {
          int temperature = new Random().nextInt(15, 30 + 1);
          subject.onNext(new SensorMessage(temperature, sensorType));
          try {
            Thread.sleep(1000);
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
          }
        }
      }).start();
    }

    @Override
    protected void subscribeActual(@NonNull Observer<? super SensorMessage> observer) {
      subject.subscribe(observer);
    }
  }

  static class CO2Sensor extends Observable<SensorMessage> {
    private final PublishSubject<SensorMessage> subject = PublishSubject.create();
    private final SensorType sensorType = SensorType.CO2_SENSOR;

    @Override
    protected void subscribeActual(Observer<? super SensorMessage> observer) {
      subject.subscribe(observer);
    }

    public void start() {
      new Thread(() -> {
        while (true) {
          int co2 = new Random().nextInt(30, 100 + 1);
          subject.onNext(new SensorMessage(co2, sensorType));
          try {
            Thread.sleep(1000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }).start();
    }
  }

  static class AlarmSystem implements Observer<SensorMessage> {
    private static final int TEMP_LIMIT = 25;
    private static final int CO2_LIMIT = 70;
    private volatile int temperature;
    private volatile int co2;

    @Override
    public void onSubscribe(Disposable d) {
      System.out.println("AlarmSystem subscribed to sensor");
    }

    @Override
    public void onNext(SensorMessage sensorMessage) {
      System.out.println(sensorMessage);
      switch (sensorMessage.sensorType) {
        case TEMPERATURE_SENSOR -> temperature = sensorMessage.value();
        case CO2_SENSOR -> co2 = sensorMessage.value();
      }
      if (temperature >= TEMP_LIMIT && co2 >= CO2_LIMIT) {
        System.out.println("ALARM!!! Temperature/CO2: " + temperature + "/" + co2);
      } else if (temperature >= TEMP_LIMIT) {
        System.out.println("Temperature warning: " + temperature);
      } else if (co2 >= CO2_LIMIT) {
        System.out.println("CO2 warning: " + co2);
      }
    }

    @Override
    public void onError(Throwable e) {
      e.printStackTrace();
    }

    @Override
    public void onComplete() {
      System.out.println("Completed");
    }
  }

  public static void main(String[] args) {
    TemperatureSensor temperatureSensor = new TemperatureSensor();
    CO2Sensor co2Sensor = new CO2Sensor();

    AlarmSystem alarmSystem = new AlarmSystem();

    temperatureSensor.subscribe(alarmSystem);
    temperatureSensor.start();
    co2Sensor.subscribe(alarmSystem);
    co2Sensor.start();
  }
}