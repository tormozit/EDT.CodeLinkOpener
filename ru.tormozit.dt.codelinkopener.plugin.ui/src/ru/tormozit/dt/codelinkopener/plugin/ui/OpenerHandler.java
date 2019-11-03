package ru.tormozit.dt.codelinkopener.plugin.ui;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;

import com._1c.g5.v8.dt.bm.index.emf.IBmEmfIndexManager;
import com._1c.g5.v8.dt.core.platform.IResourceLookup;
import com._1c.g5.v8.dt.core.platform.IV8ProjectManager;

import com.google.inject.Inject;

public class OpenerHandler extends AbstractHandler
{
	@Inject
	private IBmEmfIndexManager bmEmfIndexManager;
	@Inject
	private IResourceLookup resourceLookup;
	@Inject
	private IV8ProjectManager projectManager;
	
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
            // создадим диалог конструктора движения регистра
            OpenerHandlerDialog dialog =
                new OpenerHandlerDialog(Display.getCurrent().getActiveShell(), bmEmfIndexManager, resourceLookup, projectManager);
            // обработаем завершение работы пользователя с диалогом
            if (dialog.open() == Window.OK)
            {
            }
        return null;
    }
}
