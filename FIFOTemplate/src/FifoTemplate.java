enum ErrCode { FIFO_ALLOCATION(2),FIFO_IS_EMPTY(1),OTHER_ERROR(0);
    int mCode;
    ErrCode(int code){
        mCode = code;
    }
}

class FifoException extends RuntimeException{
    private ErrCode mErrCode = ErrCode.OTHER_ERROR;
    FifoException(ErrCode errCode){
        mErrCode = errCode;
    }
    public String getReason() {
        switch (mErrCode) {
            case FIFO_ALLOCATION:
                return "Memory allocation error\n";
            case FIFO_IS_EMPTY:
                return "Stack is empty\n";
            default:
                return "Other error\n";
        }
    }
}
//=================================
public class FifoTemplate<T>
{
    private class FifoItem<T> {
        private T m_pItem;
        private FifoItem m_pNext;

        public FifoItem(T item) {
            m_pItem = item;
            m_pNext = null;
        }
    }
    private FifoItem m_Head=null;
    private FifoItem m_Tail=null;


    public final boolean PQEmpty() {
        return (m_Head==null);
    }

    public final void PQEnqueue(T pInfo) throws FifoException {
        FifoItem item = new FifoItem(pInfo);
        if (item != null) {
            if (PQEmpty()){
                m_Head = item;
            } else {
                m_Tail.m_pNext = item;
            }
            m_Tail = item;
        } else throw new FifoException(ErrCode.FIFO_ALLOCATION);

    }

    public final T PQDequeue() throws FifoException {
        if (!PQEmpty()) {
            T pInfo = (T) m_Head.m_pItem;
            PQDel();
            return pInfo;
        }
        else throw new FifoException(ErrCode.FIFO_IS_EMPTY);
    }

    public final void PQClear() {
        while (!PQEmpty()) PQDequeue();
        m_Head = m_Tail = null;
    }

    public final void PQDel() throws FifoException {
        if (!PQEmpty()) {
            FifoItem item = m_Head;
            m_Head = item.m_pNext;
            if (PQEmpty()) m_Tail = null;
        }
        else throw new FifoException(ErrCode.FIFO_IS_EMPTY);
    }
}