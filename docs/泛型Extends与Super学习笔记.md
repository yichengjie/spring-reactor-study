1. Java泛型 <? extends T> 和 <? super T>
    ```text
    /**
     * 频繁往外读取内容的，适合用上界Extends。
     * 经常往里插入的，适合用下界Super。
     */
    @Test
    public void extendsTest(){
        //上界<? extends T>不能往里存，只能往外取
        Plate<? extends Fruit> fruitPlate = new Plate<Apple>(new Apple());
        //不能存入任何元素
        //fruitPlate.set(new Apple());    //Error  编译错误
        //fruitPlate.set(new Fruit());    //Error  编译错误
        //读取出来的东西只能放在Fruit或它的基类里
        Fruit newFruit1=fruitPlate.get();
        Object newFruit2 = fruitPlate.get();
        //Apple newFruit3 = fruitPlate.get();  //Error
    }
    @Test
    public void superTest(){
        //下界<? super T>不影响往里存，但往外取只能放在Object对象里
        Plate<? super Fruit> fruitPlate = new Plate<Fruit>(new Fruit());
        //存入元素正常
        fruitPlate.set(new Apple());
        fruitPlate.set(new Fruit());
        //读取出来的东西只能存放在Object类里
        Object newFruit1 = fruitPlate.get();
        //Fruit newFruit2 = fruitPlate.get();  //Error
        //Apple newFruit3 = fruitPlate.get();  //Error
    }
    ```