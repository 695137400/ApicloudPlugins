package com.apicloud.plugin.Project;

import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.module.WebModuleBuilder;
import com.intellij.platform.ProjectTemplate;
import com.intellij.platform.ProjectTemplatesFactory;

/**
 *
 */
public class ApicloudTemplateFactory extends ProjectTemplatesFactory {
  /**
   * 定义插件类型
   * @return
   */
  @Override
  public String[] getGroups() {
    System.out.println("启动");
    return new String[]{WebModuleBuilder.GROUP_NAME};
  }

  /**
   * 插件模版
   * @param group
   * @param context
   * @return
   */
  @Override
  public ProjectTemplate[] createTemplates(String group, WizardContext context) {
    return new ProjectTemplate[]{new ApicloudProjectTemplateGenerator()};
  }
}
