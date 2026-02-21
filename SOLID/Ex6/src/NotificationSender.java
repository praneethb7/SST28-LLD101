public abstract class NotificationSender {

    protected final AuditLog audit;

    public NotificationSender(AuditLog audit) {
        this.audit = audit;
    }

    public final void send(Notification n) {

        if (n == null) {
            throw new IllegalArgumentException("notification cannot be null");
        }

        doSend(n);
        audit.add(getChannelName() + " sent");
    }

    protected abstract void doSend(Notification n);

    protected abstract String getChannelName();
}