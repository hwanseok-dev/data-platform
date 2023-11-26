# 쓰레드의 시작부터 synchronized와 lock까지

## 멀티 쓰레드가 필요한 경우

Java 프로그램을 실행하려면 최소 1개 이상의 쓰레드가 반드시 동작을 하게 됩니다.
아래의 코드를 실행하면 App 클래스를 실행시키는 쓰레드 외에도 GC용 쓰레드 등 백그라운드에서 실행되는 쓰레드도 함께 실행됩니다.

```
public class App {

    public static void main(String[] args) {
        System.out.println("Hello World");
    }
}
```

멀티쓰레드가 필요한 상황을 정리해보면 크게 2가지입니다.

1. 시간이 걸리는 I/O 처리가 필요한 경우
2. 복수 클라이언트의 요청을 처리해야하는 경우

일반적으로 네트워크나 파일을 처리할 때 I/O에서 시간이 걸립니다. 이 시간동안 다른 작업을 못하면 안되겠죠.
하나의 쓰레드는 시간이 걸리는 작업을 하는 도중에도 다른 쓰레드는 다른 요청을 수행할 수 있어야 합니다.

## 쓰레드 생성하고 실행하기


쓰레드를 사용하는 가장 간단한 방법은 Thread를 extends하는 것입니다. 
새로 기동되는 쓰레드의 동작은 run() 메서드에 기술합니다. 

```
public class MyThread extends Thread {

    private final String name;

    public MyThread(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.printf("name : %s, idx : %d\n", name, i);
        }
    }
}
```

쓰레드를 생성한 뒤에 start 메서드를 호출하면 쓰레드가 시작합니다.  
쓰레드를 실행하기 위해서 호출하는 것은 run()이 아니라 start()임에 주의합니다.

```
public class App {

    public static void main(String[] args) {
        MyThread myThread = new MyThread("thread-01");
        myThread.start();
        /*
         * 출력 : 
         * name : thread-01, idx : 0
         * name : thread-01, idx : 1
         * name : thread-01, idx : 2
         */
    }
}
```

## 멀티 쓰레는 병렬처리가 아닌 병행처리

아래의 코드를 보면 myThread1을 먼저 시작했지만 myThread2의 출력이 먼저 시작되었습니다.

```java
package io.dataplatform.common.thread.tutorial;
public class App {

    public static void main(String[] args) {
        MyThread myThread1 = new MyThread("thread-01");
        MyThread myThread2 = new MyThread("thread-02");
        myThread1.start();
        myThread2.start();
        /*
         * 출력 :
         * 
         * ...생략...
         * name : thread-02, idx : 39
         * name : thread-02, idx : 40 // here
         * name : thread-01, idx : 0
         * name : thread-01, idx : 1
         * name : thread-01, idx : 2
         * name : thread-01, idx : 3
         * ...생략...
         */
    }
}
```

여기서 필요한 개념이 병렬과 병행의 차이입니다.

- 병렬 : 복수의 업무를 동시에 처리하는 것
- 병행 : 어떤 순서로 처리해도 상관없는 여러 개의 작업으로 분할해서 처리하는 것

병렬과 병행이라는 단어를 기억할 필요는 없습니다. 중요한 것은 멀티 쓰레드 개발에서 각 쓰레드가 수행하는 행위는
처리 순서에 상관없는 내용이어야 한다는 점입니다.    

```java
public class MyThread extends Thread {

    private final String name;

    public MyThread(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        /*
         * MyThread 인스턴스가 여러개 생성되고 실행되어도
         * 문제없이 수행되어야 한다
         */
    }
}
```

run() 구현에서 순서가 유의미하게 잘못 개발한 예시를 만들어봤습니다.
만약 아래와 같이 여러 개의 MyThread 인스턴스가 하나의 객체를 공유한다면
실행할 때 마다 다른 결과가 나올 수 있습니다.

```java
public class App {

    public static void main(String[] args) {
        List<Integer> intArr = new ArrayList<>();

        MyThread myThread01 = new MyThread("thread01", intArr);
        MyThread myThread02 = new MyThread("thread02", intArr);
        myThread01.start();
        myThread02.start();

        intArr.forEach(System.out::println);

        /* 실행 결과 예시 1
         * 0
         * 1
         * 2
         * 0
         * 1
         * 2
         */

        /* 실행 결과 예시 2
         * 0
         * 0
         * 1
         * 1
         * 2
         * 2
         */
    }
}

public class MyThread extends Thread {

    private final String name;

    private final List<Integer> intArr;

    public MyThread(String name) {
        super(name);
        this.name = name;
        this.intArr = new ArrayList<>();
    }

    public MyThread(String name, List<Integer> intArr) {
        super(name);
        this.name = name;
        this.intArr = intArr;
    }

    @Override
    public void run() {
        for (int i = 0; i < 3; i++) {
            intArr.add(i);
        }
    }
}
```

## MyThread와 Thread는 별개의 클래스

아래의 코드에서 MyThread의 인스턴스는 쓰레드가 아닙니다. 쓰레드를 감싸고 있는 인스턴스입니다.
MyThread 인스턴스를 만드는 것만으로는 쓰레드를 만들었다고 할 수 없습니다. start를 호출해야 쓰레드가 만들어집니다. 

```
package io.dataplatform.common.thread.tutorial;
public class App {

    public static void main(String[] args) {
        MyThread myThread1 = new MyThread("thread-01");
        myThread1.start();
    }
}
```

자바 프로그램은, 데몬 쓰레드를 제외한, 모든 쓰레드가 종료될 때까지 프로그램이 종료되지 않습니다.
MyThread의 구현에서 오래걸리는 작업을 수행하도록 개발했다면 그 작업이 모두 끝나야 프로그램이 종료됩니다.

## 데몬 쓰레드

자바 프로그램에서 OOM 등 치명적인 에러가 발생하면 프로그램이 종료되어야합니다.
그런데 이런 상황에서도 쓰레드의 종료를 기다리느라 프로그램이 제대로 죽지 못한다면 예상치 못한 상태로 프로그램이 수행될 수 있습니다.

예를 들어, 파일이나 네트워크로 데이터를 보내고 있는데 이러한 상황이 발생하면 프로그램이 종료되면서 더이상의 전송을 멈춰야하는데
소생불가의 상태로 잘못된 데이터를 계속 보내면 그게 더 큰 문제를 발생시킬 수 있습니다.

이러한 상황을 막기 위해 쓰레드를 만들 떄에는 항상 데몬 쓰레드로 만든다고 생각하면 편합니다.

```java
public class MyThread extends Thread {

    public MyThread(String name) {
        super(name);
        this.setDaemon(true);
    }

    @Override
    public void run() {
        // TODO
    }
}
```

setDaemon을 true로 설정된 쓰레드는 일반 쓰레드가 모두 종료되면, 자신의 업무를 모두 수행하지 못했다고 하더라도, 강제로 종료됩니다
```
public class App {

    public static void main(String[] args) {
        MyThread myThread01 = new MyThread("thread01");
        myThread01.start();

        /*
         * this.setDaemon(false);
         *
         * 0
         * 1
         * 2
         *
         */

         /*
         * this.setDaemon(true);
         * // 즉시 종료
         */
    }
}

public class MyThread extends Thread {

    public MyThread(String name) {
        super(name);
        this.setDaemon(true);
    }

    @Override
    public void run() {
        for (int i = 0; i < 3; i++) {
            try {
                // 1초 대기 
                Thread.sleep(1000);
                System.out.println("idx : " + i);
            } catch (InterruptedException e) {
                ExceptionUtil.ignore(e);
            }
        }
    }
}
```

## synchronized

MyObject의 객체를 만들고 각각의 쓰레드에서 MyObject의 값을 1씩 10만번 증가하는 예시입니다.

```java
public class App {
    public static void main(String[] args) {
        for (int i = 0; i < 2; i++) {
            new MyThread("thread0" + i).start();
        }
    }
}

public class MyThread extends Thread {

    public MyThread(String name) {
        super(name);
    }

    @Override
    public void run() {
        MyObject myObject = MyObject.getInstance();
        for (int i = 0; i < 100_000; i++) {
            myObject.increase();
        }
        System.out.println(myObject.getNum());
    }
}

public class MyObject {

    private static MyObject instance;

    public static synchronized MyObject getInstance(){
        if (instance == null) {
            instance = new MyObject();
        }
        return instance;
    }

    private int num = 0;

    public void increase(){
        num++;
    }

    public int getNum(){
        return num;
    }
}
```

2개의 쓰레드에서 10만번씩 증가했으니 두 번째 출력에서는 20만이 나와야할 것 같은데 그렇지 않습니다. 

```
181745 // 먼저 끝난 쓰레드에서 출력한 값
181745 // 나중에 끝난 쓰레드에서 출력한 값
```

synchronized 키워드를 붙이면 예상한 20만의 숫자가 잘 나옵니다. 

```java
public synchronized void increase(){
    num++;
}
```

```
181745 // 먼저 끝난 쓰레드에서 출력한 값
200000 // 나중에 끝난 쓰레드에서 출력한 값
```

synchronized는 하나의 쓰레드가 락을 취하고 있을 때 다른 쓰레드가 같은 인스턴스의 synchronized 블럭에 들어오지 못합니다.
synchronized 메서드를 수행했던 쓰레드는 그 블럭의 실행이 끝나면 락을 해제합니다. 그러면 다음 쓰레드가 들어올 수 있습니다. 

synchronized는 수행 시간에도 큰 영향을 미칩니다. 10만개로 테스트해봤을 때 약 2배의 차이가 납니다.
(성능은 환경마다 달라서 synchronized를 했을 때 항상 2배 느려진다고는 할 수 없습니다.)

```java
public class MyThread extends Thread {

    public MyThread(String name) {
        super(name);
    }

    @Override
    public void run() {
        long before = System.currentTimeMillis();
        MyObject myObject = MyObject.getInstance();
        for (int i = 0; i < 100_000; i++) {
            myObject.increase();
        }
        System.out.println(myObject.getNum());
        long after = System.currentTimeMillis();
        System.out.println(after - before + "ms");
    }
}
```

```
// synchronized 적용
176751
28ms
200000
29ms

// synchronized 미적용
143291
123090
14ms
14ms
```

## lock 

여기서부터 이 가이드를 쓰게된 이유가 나오기 시작합니다.  

### lock은 인스턴스마다 존재합니다  

위의 예제에서 MyObject는 singleton 패턴으로 하나의 인스턴스만 생성했습니다.
그래서 같은 MyObject 인스턴스의 락을 사용하기 때문에 synchronized가 의도대로 동작했습니다.  

만약 아래와 같이 수행한다면 각각의 쓰레드가 서로 다른 MyObject 인스턴스의 락을 사용합니다.

```java
public class App {
    public static void main(String[] args) {
        for (int i = 0; i < 2; i++) {
            new MyThread("thread0" + i).start();
        }
    }
}

public class MyThread extends Thread {

    public MyThread(String name) {
        super(name);
    }

    @Override
    public void run() {
        long before = System.currentTimeMillis();
//        MyObject myObject = MyObject.getInstance();
        // 각각의 쓰레드가 새로운 객체를 생성
        MyObject myObject = new MyObject();
        for (int i = 0; i < 100_000; i++) {
            myObject.increase();
        }
        System.out.println(myObject.getNum());
        long after = System.currentTimeMillis();
        System.out.println(after - before + "ms");
    }
}

```

```txt
100000
100000
16ms
15ms
```

### static과 lock

락이 객체마다 생성된다는 점을 고려해서 아래와 같이 코드를 변경할 수 있습니다.  

myObject를 static 필드로 옮겼습니다. myObject 객체를 생성할 때 singleton 패턴을 사용하지 않아도
static 필드는 MyThread 클래스에서 1개만 생성되므로 두 개의 쓰레드에서 공유합니다.


```java
public class App {
    public static void main(String[] args) {
        for (int i = 0; i < 2; i++) {
            new MyThread("thread0" + i).start();
        }
    }
}


public class MyThread extends Thread {

    private static final MyObject myObject = new MyObject();

    public MyThread(String name) {
        super(name);
    }

    @Override
    public void run() {
        long before = System.currentTimeMillis();
        for (int i = 0; i < 100_000; i++) {
            myObject.increase();
        }
        System.out.println(myObject.getNum());
        long after = System.currentTimeMillis();
        System.out.println(after - before + "ms");
    }
}
```

``` txt
// synchronized 미적용
122768
119157
7ms
8ms

// synchronized 적용
184439
200000
26ms
27ms
```

반대로 lock 객체의 static 메서드를 호출하면 어떻게 될까요?  

```java
public class MyThread extends Thread {

    public MyThread(String name) {
        super(name);
    }

    @Override
    public void run() {
        long before = System.currentTimeMillis();
        for (int i = 0; i < 100_000; i++) {
            MyObject.increaseStatic();
        }
        System.out.println(MyObject.getStaticNum());
        long after = System.currentTimeMillis();
        System.out.println(after - before + "ms");
    }
}

public class MyObject {

    private static MyObject instance;

    public static synchronized MyObject getInstance(){
        if (instance == null) {
            instance = new MyObject();
        }
        return instance;
    }

    private int num = 0;
    private static int staticNum = 0;

    public void increase(){
        num++;
    }

    public static synchronized void increaseStatic(){
        staticNum++;
    }

    public int getNum(){
        return num;
    }

    public static int getStaticNum(){
        return staticNum;
    }
}
```

```txt
165590
31ms
200000
34ms
```

이 때에는 MyObject 인스턴스의 lock이 아닌, 클래스의 lock 객체가 사용됩니다.

### synchronized 블록

synchronized 블럭을 지정해서 동기화를 할 수도 있습니다. 메서드에서 특정 부분만 Critical Section인 경우에 사용할 수 있습니다.

```java
public class MyThread extends Thread {

    private static final MyObject myObject = new MyObject();

    public MyThread(String name) {
        super(name);
    }

    @Override
    public void run() {
        long before = System.currentTimeMillis();
        for (int i = 0; i < 100_000; i++) {
            synchronized (myObject) {
                // increase에서는 synchronized 지정하지 않음
                myObject.increase();
            }
        }
        System.out.println(myObject.getNum());
        long after = System.currentTimeMillis();
        System.out.println(after - before + "ms");
    }
}
```