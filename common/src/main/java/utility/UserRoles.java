package utility;
// child может только входить в
// систему и просматривать объекты,
// менять их не может
// teen может то, что может child и
// простые команды без манипуляций с
// элементом
// adult может плюсом манипулировать
// элементами
// admin может менять роли плюсом к
// adult
public enum UserRoles {
    CHILD, TEEN, ADULT, ADMIN
}