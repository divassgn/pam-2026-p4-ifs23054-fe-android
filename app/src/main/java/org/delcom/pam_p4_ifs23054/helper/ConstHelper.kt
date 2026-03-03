package org.delcom.pam_p4_ifs23054.helper

class ConstHelper {
    // Route Names
    enum class RouteNames(val path: String) {
        Home(path = "home"),
        Profile(path = "profile"),

        // Plants
        Plants(path = "plants"),
        PlantsAdd(path = "plants/add"),
        PlantsDetail(path = "plants/{plantId}"),
        PlantsEdit(path = "plants/{plantId}/edit"),

        // Skincares
        Skincares(path = "skincares"),
        SkincareAdd(path = "skincares/add"),
        SkincareDetail(path = "skincares/{skincareId}"),
        SkincareEdit(path = "skincares/{skincareId}/edit"),
    }
}
