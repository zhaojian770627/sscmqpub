package nc.itf.inter;

import nc.vo.pub.BusinessException;
import nc.vo.sscmq.InterTaskVO;

public interface ITaskProcessor {
	Object doAction() throws BusinessException;
	Object undoAction() throws BusinessException;
	void setTask(InterTaskVO taskvo);
}
