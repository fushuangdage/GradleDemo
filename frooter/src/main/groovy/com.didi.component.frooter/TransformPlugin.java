package com.didi.component.frooter;
import com.android.build.gradle.AppExtension;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * Author: fushuang
 * Date: 2020/6/16 下午8:42
 * Description:
 * Wiki:
 * History:
 * <author> <time> <version> <desc>
 */
public class TransformPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        AppExtension appExtension = project.getExtensions().findByType(AppExtension.class);
        appExtension.registerTransform(new JavassistTransform(project));
        appExtension.registerTransform(new AsmTransform(project));

    }
}
