package com.alim.ssn.FakeDataGenerator;

import com.alim.ssn.model.Student;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class FakePeoples {
    Observable observable;
    public void  getStudents(){
         observable=Observable.create(new ObservableOnSubscribe<Student>() {
            @Override
            public void subscribe(ObservableEmitter<Student> e) throws Exception {
                List<Student> students=new ArrayList<>();

                Student student=new Student();
                student.setName("علی");
                student.setLast_name("حسنی");
                student.setProfile_picture("http://4.bp.blogspot.com/-OAeT5cO52Pk/TkuDQ9H3IBI/AAAAAAAAGFw/KSdMyD__wVc/s1600/bruno-mars-mug-shot-01.jpg");
                student.setUser_name("alih23");
                e.onNext(student);

                Student student1=new Student();
                student1.setName("مجتبی");
                student1.setLast_name("خانزاده");
                student1.setProfile_picture("http://genderfork.com/wp-content/uploads/2012/04/Rosie.jpg");
                student1.setUser_name("mojtaba234");
                e.onNext(student1);

                Student student2=new Student();
                student2.setName("حدیث");
                student2.setLast_name("علی پور");
                student2.setProfile_picture("https://i.pinimg.com/564x/fb/98/02/fb9802b381f51fc4f7d56a8063306185--character-portraits-character-inspiration.jpg");
                student2.setUser_name("hadis324");
                e.onNext(student2);


                Student student3=new Student();
                student3.setName("نگین");
                student3.setLast_name("محمدی");
                student3.setProfile_picture("https://www.hindustantimes.com/rf/image_size_960x540/HT/p2/2017/04/14/Pictures/_2596589e-20ee-11e7-beb7-f1cbdf0743d8.jpg");
                student3.setUser_name("mohamadi32");
                e.onNext(student3);



                Student student4=new Student();
                student4.setName("کاوه");
                student4.setLast_name("نیکزاده");
                student4.setProfile_picture("https://live.staticflickr.com/3893/14921394066_cb305327fe_b.jpg");
                student4.setUser_name("kave_n");
                e.onNext(student4);



                Student student5=new Student();
                student5.setName("حسین");
                student5.setLast_name("کریمی");
                student5.setProfile_picture("https://i.pinimg.com/originals/2e/96/e1/2e96e1614412d35ecda0cf6108e083b2.jpg");
                student5.setUser_name("hosein_k32");
                e.onNext(student5);


                Student student6=new Student();
                student6.setName("رضا");
                student6.setLast_name("محسنی");
                student6.setProfile_picture("https://dp.profilepics.in/profile_pictures/stylish-boys/boys-profile-pics-82.jpg");
                student6.setUser_name("reza_m_21");
                e.onNext(student6);

            }
        });

    }

}
