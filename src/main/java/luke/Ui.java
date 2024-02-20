package luke;

/**
 * Represents the user interface of the Luke chatbot.
 * <p>
 * The UI class handles user input, processes commands, and interacts with the task list.
 * It uses a parser to interpret user commands and executes corresponding actions.
 * </p>
 */
public class Ui {

    protected TaskList taskList;

    protected Storage storage;

    protected Parser parser;
    Ui() {
        this.taskList = new TaskList();
        this.parser = new Parser(taskList);
        this.storage = new Storage("data/tasks.txt");
    }

    Ui(TaskList taskList) {
        this.taskList = taskList;
        this.parser = new Parser(taskList);
        this.storage = new Storage("data/tasks.txt");
    }

    protected String handleInput(String input) {
        try {
            parser.isInputValid(input);
            String command = parser.getCommand(input);
            String result;
            switch (command) {
            case "list":
                return this.list(input);
            case "mark":
                return this.mark(input);
            case "unmark":
                return this.unmark(input);
            case "delete":
                return this.delete(input);
            case "todo":
                return this.todo(input);
            case "deadline":
                return this.deadline(input);
            case "event":
                return this.event(input);
            case "find":
                return this.find(input);
            case "edit":
                return this.edit(input);
            case "bye":
                return this.end();
            default:
                throw new LukeException(LukeException.ExceptionType.commandInvalid);
            }
        } catch (LukeException e) {
            return e.getMessage();
        }
    }

    /**
     * Generates a welcome message for the user.
     *
     * @return The welcome message, including the name of the chatbot.
     */
    public String welcome() {
        String name = "Luke";
        return "Hello! I'm " + name + "\nWhat can I do for you?";
    }

    private String list(String input) {
        try {
            parser.isListCommandValid(input);
        } catch (LukeException e) {
            return e.getMessage();
        }
        return "Here are the tasks in your list: \n" + parser.commandList(taskList);
    }

    private String mark(String input) {
        try {
            Task taskMarked = parser.commandMark(input);
            return "Nice! I've marked this task as done: \n" + taskMarked.toString();
        } catch (LukeException e) {
            return e.getMessage();
        }
    }

    private String unmark(String input) {
        try {
            Task taskUnmarked = parser.commandUnmark(input);
            return "OK, I've marked this task as not done yet: \n" + taskUnmarked.toString();
        } catch (LukeException e) {
            return e.getMessage();
        }
    }

    private String delete(String input) {
        try {
            Task taskDeleted = parser.commandDelete(input);
            return "Noted. I've removed this task:\n" + taskDeleted.toString()
                    + "\nNow you have " + taskList.getNoTasks() + " tasks in the list.";
        } catch (LukeException e) {
            return e.getMessage();
        }
    }

    private String todo(String input) {
        try {
            Task todoAdded = parser.commandTodo(input);
            return taskSuccessfullyAdded(todoAdded, taskList.getNoTasks());
        } catch (LukeException e) {
            return e.getMessage();
        }
    }

    private String deadline(String input) {
        try {
            Task deadlineAdded = parser.commandDeadline(input);
            return taskSuccessfullyAdded(deadlineAdded, taskList.getNoTasks());
        } catch (LukeException e) {
            return e.getMessage();
        }
    }

    private String event(String input) {
        try {
            Task eventAdded = parser.commandEvent(input);
            return taskSuccessfullyAdded(eventAdded, taskList.getNoTasks());
        } catch (LukeException e) {
            return e.getMessage();
        }
    }

    private String taskSuccessfullyAdded(Task task, int noTasks) {
        return "I've added this task:\n" + task.toString()
            + "\nNow you have " + noTasks + " tasks in the list.";
    }

    private String find(String input) {
        try {
            String keyword = parser.commandFind(input);
            TaskList tasksFound = taskList.search(keyword);
            if (tasksFound.getNoTasks() == 0) {
                return "No tasks with the keyword found.";
            } else {
                return "Here are the matching tasks in your list:\n" + tasksFound.list();
            }
        } catch (LukeException e) {
            return e.getMessage();
        }
    }

    private String edit(String input) {
        try {
            Task taskEdited = parser.commandEdit(input);
            return "Task change successful! This is the new task:\n" + taskEdited.toString();
        } catch (LukeException e) {
            return e.getMessage();
        }
    }

    public String end() {
        return storage.saveFile(taskList);
    }
}
