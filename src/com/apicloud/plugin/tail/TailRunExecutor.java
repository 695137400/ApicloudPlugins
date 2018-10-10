package com.apicloud.plugin.tail;

import com.intellij.execution.Executor;
import com.intellij.execution.ExecutorRegistry;
import com.intellij.icons.AllIcons;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Window Config
 * @author ob
 */
public class TailRunExecutor extends Executor {
	public static final Icon ToolWindowRun =     new ImageIcon(TailRunExecutor.class.getResource("/com/apicloud/plugin/icons/apicloud.png"));
	//public static final Icon ToolWindowRun = ApicloudIcon.ApicloudIcon;

	public static final String TOOLWINDOWS_ID = "Apicloud";
	@NonNls
	public static final String EXECUTOR_ID = "ApicloudLogTail";

	@Override
	@NotNull
	public String getStartActionText() {
		return TOOLWINDOWS_ID;
	}

	@Override
	public String getToolWindowId() {
		return TOOLWINDOWS_ID;
	}

	@Override
	public Icon getToolWindowIcon() {
		return ToolWindowRun;
	}

	@Override
	@NotNull
	public Icon getIcon() {
		return AllIcons.Actions.Execute;
	}

	@Override
	public Icon getDisabledIcon() {
		return AllIcons.Process.DisabledRun;
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	@NotNull
	public String getActionName() {
		return TOOLWINDOWS_ID;
	}

	@Override
	@NotNull
	public String getId() {
		return EXECUTOR_ID;
	}

	@Override
	public String getContextActionId() {
		return "ApicloudActionId";
	}

	@Override
	public String getHelpId() {
		return null;
	}

	public static Executor getRunExecutorInstance() {
		return ExecutorRegistry.getInstance().getExecutorById(EXECUTOR_ID);
	}
}
